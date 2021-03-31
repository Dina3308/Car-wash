package ru.kpfu.itis.carwash.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.model.AuthUser
import ru.kpfu.itis.domain.model.Resource
import kotlinx.coroutines.flow.collect

class AuthViewModel(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    fun register(authUser: AuthUser) = liveData(Dispatchers.Main) {
        emit(Resource.Loading())
        try {
            authInteractor.register(authUser).collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(Resource.Error<Boolean>(e))
        }
    }

    fun login(authUser: AuthUser) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            authInteractor.login(authUser).collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(Resource.Error<Boolean>(e))
        }
    }

}