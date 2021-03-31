package ru.kpfu.itis.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.model.AuthUser
import ru.kpfu.itis.domain.model.Resource

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
): AuthRepository {

    @ExperimentalCoroutinesApi
    override suspend fun register(authUser: AuthUser): Flow<Resource<Boolean>> = callbackFlow {
        auth.createUserWithEmailAndPassword(authUser.email, authUser.password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        offer(Resource.Success(true))
                    }
                }.addOnFailureListener {
                    offer(Resource.Error(it, false))
                }

        awaitClose { this.cancel() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun login(authUser: AuthUser): Flow<Resource<Boolean>> = callbackFlow {
        auth.signInWithEmailAndPassword(authUser.email, authUser.password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        offer(Resource.Success(true))
                    }
                }.addOnFailureListener {
                    offer(Resource.Error(it, false))
                }

        awaitClose { this.cancel() }
    }

}


