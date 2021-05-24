package ru.kpfu.itis.carwash.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.common.Event
import ru.kpfu.itis.carwash.common.NetworkConnectionUtil
import ru.kpfu.itis.carwash.common.ResourceManager
import ru.kpfu.itis.domain.SettingInteractor
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val interactor: SettingInteractor,
    private val resourceManager: ResourceManager,
    private val networkConnectionUtil: NetworkConnectionUtil
) : ViewModel() {

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val cityAddress: MutableLiveData<String> = MutableLiveData()
    private val showErrorEvent: MutableLiveData<Event<String>> = MutableLiveData()

    init {
        getUser()
    }

    fun progress(): LiveData<Boolean> = progress
    fun cityAddress(): LiveData<String> = cityAddress
    fun showErrorEvent(): LiveData<Event<String>> = showErrorEvent

    private fun getUser() {
        viewModelScope.launch {
            progress.value = true
            val address = interactor.getCity()
            if (address.isSuccess) {
                address.getOrNull()?.let {
                    cityAddress.value = resourceManager.getString(R.string.choose_city, it)
                }
            }
            progress.value = false
        }
    }

    fun updateCity(place: Place) {
        viewModelScope.launch {
            if (networkConnectionUtil.isConnected()) {
                progress.value = true
                val result = interactor.updateCity(place)
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        cityAddress.value = it.address?.run {
                            resourceManager.getString(
                                R.string.choose_city,
                                this
                            )
                        }
                    }
                }
                progress.value = false
            } else {
                showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
            }
        }
    }
}
