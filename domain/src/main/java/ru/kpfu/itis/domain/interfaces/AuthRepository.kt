package ru.kpfu.itis.domain.interfaces

import ru.kpfu.itis.domain.model.AuthUser

interface AuthRepository {
    suspend fun register(email: String, password: String): AuthUser?
    suspend fun login(email: String, password: String)
    suspend fun getCurrentUser(): AuthUser?
    suspend fun signOut()
}
