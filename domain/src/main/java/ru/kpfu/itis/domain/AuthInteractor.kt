package ru.kpfu.itis.domain

import com.google.android.libraries.places.api.model.Place
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.model.AuthUser

class AuthInteractor(
    private val authRepository: AuthRepository,
    private val fireStoreRepository: FireStoreRepository
) {
    suspend fun register(email: String, password: String, place: Place): Result<AuthUser?> {
        return runCatching {
            authRepository.register(email, password)?.also {
                fireStoreRepository.addUser(place, it.uid)
            }
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return runCatching {
            authRepository.login(email, password)
        }
    }

    suspend fun getCurrentUser(): Boolean {
        if (authRepository.getCurrentUser() != null) {
            return true
        }
        return false
    }
}
