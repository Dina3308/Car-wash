package ru.kpfu.itis.domain

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.model.AuthUser

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun register(authUser: AuthUser): Result<FirebaseUser> {
        return runCatching {
            authRepository.register(authUser)
        }
    }

    suspend fun login(authUser: AuthUser): Result<Boolean> {
        return runCatching {
            authRepository.login(authUser)
        }
    }

    suspend fun getCurrentUser():FirebaseUser? = authRepository.getCurrentUser()

    suspend fun signOut(): Result<Boolean> {
        return runCatching {
            authRepository.signOut()
        }
    }
}
