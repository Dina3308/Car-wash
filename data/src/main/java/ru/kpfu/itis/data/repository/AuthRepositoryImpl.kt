package ru.kpfu.itis.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kpfu.itis.data.mappers.mapFireBaseUserToAuthUser
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.model.AuthUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(email: String, password: String): AuthUser? = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result.user?.let { mapFireBaseUserToAuthUser(it) })
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun login(email: String, password: String): Unit = suspendCancellableCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getCurrentUser(): AuthUser? = auth.currentUser?.let { mapFireBaseUserToAuthUser(it) }

    override suspend fun signOut() {
        auth.signOut()
    }
}
