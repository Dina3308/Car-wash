package ru.kpfu.itis.carwash.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import ru.kpfu.itis.domain.FireStoreInteractor
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val interactor: FireStoreInteractor,
): ViewModel() {

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val user: MutableLiveData<Result<DocumentSnapshot>> = MutableLiveData()
    private val city: MutableLiveData<Result<Place>> = MutableLiveData()

    init {
        getUser()
    }

    fun progress(): LiveData<Boolean> = progress
    fun user():LiveData<Result<DocumentSnapshot>> = user
    fun city():LiveData<Result<Place>> = city

    private fun getUser(){
        viewModelScope.launch {
            progress.value = true
            val document = interactor.getUser()
            if(document.isSuccess){
                document.getOrNull()?.let {
                    user.value = Result.success(it)
                }
            }
            else{
                document.exceptionOrNull()?.let {
                    user.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun updateCity(place: Place){
        viewModelScope.launch {
            progress.value = true
            val result = interactor.updateCity(place)
            if(result.isSuccess){
                result.getOrNull()?.let {
                    city.value = Result.success(it)
                }
            }
            else{
                result.exceptionOrNull()?.let{
                    city.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }
}