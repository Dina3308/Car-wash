package ru.kpfu.itis.carwash.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch
import ru.kpfu.itis.domain.SettingInteractor
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val interactor: SettingInteractor,
) : ViewModel() {

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val cityAddress: MutableLiveData<Result<String>> = MutableLiveData()
    private val city: MutableLiveData<Result<Place>> = MutableLiveData()

    init {
        getUser()
    }

    fun progress(): LiveData<Boolean> = progress
    fun cityAddress(): LiveData<Result<String>> = cityAddress
    fun city(): LiveData<Result<Place>> = city

    private fun getUser() {
        viewModelScope.launch {
            progress.value = true
            val address = interactor.getCity()
            if (address.isSuccess) {
                address.getOrNull()?.let {
                    cityAddress.value = Result.success(it)
                }
            } else {
                address.exceptionOrNull()?.let {
                    cityAddress.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun updateCity(place: Place) {
        viewModelScope.launch {
            progress.value = true
            val result = interactor.updateCity(place)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    city.value = Result.success(it)
                }
            } else {
                result.exceptionOrNull()?.let {
                    city.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }
}
