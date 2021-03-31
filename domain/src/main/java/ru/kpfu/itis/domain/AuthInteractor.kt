package ru.kpfu.itis.domain

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.model.AuthUser
import ru.kpfu.itis.domain.model.Resource

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun register(authUser: AuthUser) : Flow<Resource<Boolean>> = authRepository.register(authUser)

    suspend fun login(authUser: AuthUser) : Flow<Resource<Boolean>> = authRepository.login(authUser)
}