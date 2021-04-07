package ru.kpfu.itis.domain

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.model.AuthUser

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun register(authUser: AuthUser): Flow<Boolean> =
        authRepository.register(authUser)

    suspend fun login(authUser: AuthUser): Flow<Boolean> = authRepository.login(authUser)
}
