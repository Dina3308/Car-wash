package ru.kpfu.itis.carwash.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import ru.kpfu.itis.carwash.common.ResultState
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.model.AuthUser

class AuthViewModel(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    fun register(authUser: AuthUser) = liveData(Dispatchers.Main) {
        emit(ResultState.Loading)
        try {
            authInteractor.register(authUser).collect {
                emit(ResultState.Success(it))
            }
        } catch (e: Exception) {
            emit(ResultState.Error<Boolean>(e))
        }
    }

    fun login(authUser: AuthUser) = liveData(Dispatchers.IO) {
        emit(ResultState.Loading)
        try {
            authInteractor.login(authUser).collect {
                emit(ResultState.Success(it))
            }
        } catch (e: Exception) {
            emit(ResultState.Error<Boolean>(e))
        }
    }
}
