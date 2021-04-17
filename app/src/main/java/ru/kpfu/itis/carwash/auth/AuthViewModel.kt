package ru.kpfu.itis.carwash.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.FireStoreInteractor
import ru.kpfu.itis.domain.model.AuthUser
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val fireStoreInteractor: FireStoreInteractor
) : ViewModel() {

    private val city: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val register: MutableLiveData<Result<FirebaseUser>> = MutableLiveData()
    private val login: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun city() : LiveData<Result<Boolean>> = city
    fun register(): LiveData<Result<FirebaseUser>> = register
    fun login(): LiveData<Result<Boolean>> = login
    fun progress(): LiveData<Boolean> = progress

    fun register(authUser: AuthUser){
        viewModelScope.launch {
            progress.value = true
            val currentUser = authInteractor.register(authUser)
            if (currentUser.isSuccess) {
                currentUser.getOrNull()?.let {
                    register.value = Result.success(it)

                }
            } else {
                currentUser.exceptionOrNull()?.let {
                    register.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun login(authUser: AuthUser) {
        viewModelScope.launch {
            progress.value = true
            val result = authInteractor.login(authUser)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    login.value = Result.success(it)

                }
            } else {
                result.exceptionOrNull()?.let {
                    login.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun addCity(place: Place, id: String){
        viewModelScope.launch {
            progress.value = true
            val result = fireStoreInteractor.addUser(place, id)
            if(result.isSuccess){
                result.getOrNull()?.let {
                    city.value = Result.success(it)
                }
            } else{
                result.exceptionOrNull()?.let {
                    city.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

}
