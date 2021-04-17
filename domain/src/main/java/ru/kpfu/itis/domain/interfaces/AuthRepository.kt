package ru.kpfu.itis.domain.interfaces

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.domain.model.AuthUser

interface AuthRepository {
    suspend fun register(authUser: AuthUser): FirebaseUser
    suspend fun login(authUser: AuthUser): Boolean
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun signOut(): Boolean
}
