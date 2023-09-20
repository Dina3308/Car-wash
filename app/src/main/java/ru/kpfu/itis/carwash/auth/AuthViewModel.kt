package ru.kpfu.itis.carwash.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.auth.model.User
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.model.AuthUser
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor
) : ViewModel() {

    private val register: MutableLiveData<Result<User>> = MutableLiveData()
    private val login: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun register(): LiveData<Result<User>> = register
    fun login(): LiveData<Result<Boolean>> = login
    fun progress(): LiveData<Boolean> = progress

    fun register(email: String, password: String, place: Place) {
        viewModelScope.launch {
            progress.value = true
            val currentUser = interactor.register(email, password, place)
            if (currentUser.isSuccess) {
                currentUser.getOrNull()?.let {
                    register.value = Result.success(mapAuthUserToUser(it))
                }
            } else {
                currentUser.exceptionOrNull()?.let {
                    register.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            progress.value = true
            val result = interactor.login(email, password)
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

    private fun mapAuthUserToUser(user: AuthUser): User {
        return User(
            user.uid
        )
    }
}
