package ru.kpfu.itis.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.domain.model.AuthUser

interface AuthRepository {
    suspend fun register(authUser: AuthUser): Flow<Boolean>
    suspend fun login(authUser: AuthUser): Flow<Boolean>
}
