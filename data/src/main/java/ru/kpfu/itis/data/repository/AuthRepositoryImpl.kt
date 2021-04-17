package ru.kpfu.itis.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.model.AuthUser
import java.lang.NullPointerException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(authUser: AuthUser): FirebaseUser = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(authUser.email, authUser.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result.user)
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }


    override suspend fun login(authUser: AuthUser): Boolean = suspendCancellableCoroutine {continuation ->
        auth.signInWithEmailAndPassword(authUser.email, authUser.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(true)
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override suspend fun signOut(): Boolean {
        auth.signOut()
        return true
    }
}
