package ru.kpfu.itis.carwash.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.common.Event
import ru.kpfu.itis.carwash.common.NetworkConnectionUtil
import ru.kpfu.itis.carwash.common.ResourceManager
import ru.kpfu.itis.carwash.profile.model.CurrentWeatherDetails
import ru.kpfu.itis.carwash.profile.model.UserProfile
import ru.kpfu.itis.domain.ProfileInteractor
import ru.kpfu.itis.domain.model.CurrentWeather
import ru.kpfu.itis.domain.model.UserEntity
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor,
    private val resourceManager: ResourceManager,
    private val networkConnectionUtil: NetworkConnectionUtil
) : ViewModel() {

    companion object {
        private const val LEVEL_OF_CAR_POLLUTION = 15
        private const val MATCH_FOR_DATE = "EE, dd MMMM"
        private const val MATCH_FOR_DATE_PICKER = "MM/dd/yyyy"
    }

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val signOut: MutableLiveData<Event<Unit>> = MutableLiveData()
    private val date: MutableLiveData<Event<String>> = MutableLiveData()
    private val user: MutableLiveData<UserProfile> = MutableLiveData()
    private val weather: MutableLiveData<CurrentWeatherDetails> = MutableLiveData()
    private val dateCarWash: MutableLiveData<Event<String>> = MutableLiveData()
    private val levelOfCarPollution: MutableLiveData<Float> = MutableLiveData()
    private val showDialogEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    private val showErrorEvent: MutableLiveData<Event<String>> = MutableLiveData()

    init {
        getUser()
    }

    fun progress(): LiveData<Boolean> = progress
    fun signOut(): LiveData<Event<Unit>> = signOut
    fun date(): LiveData<Event<String>> = date
    fun user(): LiveData<UserProfile> = user
    fun weather(): LiveData<CurrentWeatherDetails> = weather
    fun dateCarWash(): LiveData<Event<String>> = dateCarWash
    fun levelOfCarPollution(): LiveData<Float> = levelOfCarPollution
    fun showDialogEvent(): LiveData<Event<Unit>> = showDialogEvent
    fun showErrorEvent(): LiveData<Event<String>> = showErrorEvent

    fun signOutUser() {
        viewModelScope.launch {
            progress.value = true
            interactor.signOut()
            signOut.value = Event(Unit)
            progress.value = false
        }
    }

    fun updateDate(dateOfLastWash: Date) {
        viewModelScope.launch {
            if (networkConnectionUtil.isConnected()) {
                progress.value = true
                val dateResult = interactor.updateDate(dateOfLastWash)
                dateResult.getOrNull()?.let {
                    showDialog(it)
                    date.value = Event(dateFormatterForDatePicker(it))
                }
                progress.value = false
            } else {
                showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
            }
        }
    }

    fun showWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            progress.value = true
            val currentWeather = interactor.getCurrentWeather(lat, lon)
            if (currentWeather.isSuccess) {
                currentWeather.getOrNull()?.let {
                    weather.value = mapCurrentWeatherToCurrentWeatherDetails(it)
                }
                progress.value = false
            } else {
                showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
            }
        }
    }

    fun updateLevelOfCarPollution(level: Int) {
        viewModelScope.launch {
            if (networkConnectionUtil.isConnected()) {
                progress.value = true
                val levelResult = interactor.updateLevelOfCarPollution(level.toLong())
                levelResult.getOrNull()?.let {
                    levelOfCarPollution.value = it.toFloat()
                }
                progress.value = false
            } else {
                showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
            }
        }
    }

    fun getDateCarWash(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                progress.value = true

                val date = interactor.getDayOfCarWash(lat, lon)

                val carWashNotification = if (date != null) {
                    resourceManager.getString(R.string.day_car_wash, dateFormatter(date))
                } else {
                    resourceManager.getString(R.string.no_car_wash)
                }

                dateCarWash.value = Event(carWashNotification)
            } catch (ex: Exception) {
                showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
            } finally {
                progress.value = false
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            progress.value = true
            val document = interactor.getUserDocument()
            if (document.isSuccess) {
                document.getOrNull()?.let {
                    user.value = mapUserEntityToUserProfile(it)
                }
            }
            progress.value = false
        }
    }

    private fun showDialog(date: Date) {
        if (compareDates(date)) {
            updateLevelOfCarPollution(0)
        } else {
            showDialogEvent.value = Event(Unit)
        }
    }

    private fun mapCurrentWeatherToCurrentWeatherDetails(weather: CurrentWeather): CurrentWeatherDetails {
        return with(weather) {
            CurrentWeatherDetails(
                temp,
                description,
                icon,
                tempMax,
                tempMin,
                name,
                dateFormatter(Calendar.getInstance().time)
            )
        }
    }

    private fun mapUserEntityToUserProfile(user: UserEntity): UserProfile {
        return with(user) {
            UserProfile(
                address!!,
                lat!!,
                lon!!,
                dateFormatterForDatePicker(date),
                convertLevelOfCarPollution(levelOfCarPollution!!)
            )
        }
    }

    private fun dateFormatter(date: Date): String {
        return SimpleDateFormat(MATCH_FOR_DATE, Locale.forLanguageTag(resourceManager.getString(R.string.language_tag)))
            .format(date)
    }

    private fun dateFormatterForDatePicker(date: Date?): String {
        return if (date != null) {
            SimpleDateFormat(MATCH_FOR_DATE_PICKER, Locale.forLanguageTag(resourceManager.getString(R.string.language_tag)))
                .format(date)
        } else {
            resourceManager.getString(R.string.choose_date)
        }
    }

    private fun convertLevelOfCarPollution(level: Int): Float {
        return if (level > LEVEL_OF_CAR_POLLUTION) {
            LEVEL_OF_CAR_POLLUTION.toFloat()
        } else {
            level.toFloat()
        }
    }

    private fun compareDates(date: Date): Boolean {
        val today = Calendar.getInstance()
        val dateLastCarWash = Calendar.getInstance().also {
            it.time = date
        }
        if (today.get(Calendar.YEAR) == dateLastCarWash.get(Calendar.YEAR) &&
            today.get(Calendar.MONTH) == dateLastCarWash.get(Calendar.MONTH) &&
            today.get(Calendar.DAY_OF_MONTH) == dateLastCarWash.get(Calendar.DAY_OF_MONTH)
        ) {
            return true
        }
        return false
    }
}
