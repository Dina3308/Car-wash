package ru.kpfu.itis.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.domain.model.AuthUser
import ru.kpfu.itis.domain.model.Resource

interface AuthRepository {
    suspend fun register(authUser: AuthUser) : Flow<Resource<Boolean>>
    suspend fun login(authUser: AuthUser): Flow<Resource<Boolean>>
}