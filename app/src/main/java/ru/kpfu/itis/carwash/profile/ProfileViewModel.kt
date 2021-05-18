package ru.kpfu.itis.carwash.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.profile.mapper.mapCurrentWeatherToCurrentWeatherDetails
import ru.kpfu.itis.carwash.profile.mapper.mapUserEntityToUserProfile
import ru.kpfu.itis.carwash.profile.model.CurrentWeatherDetails
import ru.kpfu.itis.carwash.profile.model.UserProfile
import ru.kpfu.itis.domain.ProfileInteractor
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val signOut: MutableLiveData<Boolean> = MutableLiveData()
    private val date: MutableLiveData<Result<Date>> = MutableLiveData()
    private val user: MutableLiveData<Result<UserProfile>> = MutableLiveData()
    private val weather: MutableLiveData<Result<CurrentWeatherDetails>> = MutableLiveData()
    private val dateCarWash: MutableLiveData<Result<Date?>> = MutableLiveData()
    private val levelOfCarPollution: MutableLiveData<Result<Long?>> = MutableLiveData()

    init {
        getUser()
    }

    fun progress(): LiveData<Boolean> = progress
    fun signOut(): LiveData<Boolean> = signOut
    fun date(): LiveData<Result<Date>> = date
    fun user(): LiveData<Result<UserProfile>> = user
    fun weather(): LiveData<Result<CurrentWeatherDetails>> = weather
    fun dateCarWash(): LiveData<Result<Date?>> = dateCarWash
    fun levelOfCarPollution(): LiveData<Result<Long?>> = levelOfCarPollution

    fun signOutUser() {
        viewModelScope.launch {
            progress.value = true
            signOut.value = interactor.signOut()
            progress.value = false
        }
    }

    fun setDate(dateOfLastWash: Date) {
        viewModelScope.launch {
            progress.value = true
            val dateResult = interactor.updateDate(dateOfLastWash)
            if (dateResult.isSuccess) {
                dateResult.getOrNull()?.let {
                    date.value = Result.success(it)
                }
            } else {
                dateResult.exceptionOrNull()?.let {
                    date.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun showWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                progress.value = true
                interactor.getCurrentWeather(lat, lon).also {
                    weather.value = Result.success(mapCurrentWeatherToCurrentWeatherDetails(it))
                }
            } catch (ex: Exception) {
                weather.value = Result.failure(ex)
            } finally {
                progress.value = false
            }
        }
    }

    fun updateLevelOfCarPollution(level: Long) {
        viewModelScope.launch {
            progress.value = true
            val levelResult = interactor.updateLevelOfCarPollution(level)
            if (levelResult.isSuccess) {
                levelResult.getOrNull()?.let {
                    levelOfCarPollution.value = Result.success(it)
                }
            } else {
                levelResult.exceptionOrNull()?.let {
                    date.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun getDateCarWash(location: Pair<Double?, Double?>) {
        viewModelScope.launch {
            try {
                progress.value = true
                dateCarWash.value = Result.success(interactor.getDayOfCarWash(location))
            } catch (ex: Exception) {
                dateCarWash.value = Result.failure(ex)
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
                    user.value = Result.success(mapUserEntityToUserProfile(it))
                }
            } else {
                document.exceptionOrNull()?.let {
                    user.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }
}
