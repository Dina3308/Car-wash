package ru.kpfu.itis.domain

import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentSnapshot
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import java.util.*

class FireStoreInteractor(
    private val fireStoreRepository: FireStoreRepository,
    private val authRepository: AuthRepository
) {
    suspend fun addUser(place: Place, userId: String): Result<Boolean>{
        return runCatching{
            fireStoreRepository.addUser(place, userId)
        }
    }

    suspend fun updateCity(place: Place): Result<Place?>{
        return runCatching{
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.updateCity(place, it.uid)
            }
        }
    }

    suspend fun getUser(): Result<DocumentSnapshot?>{
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.getUser(it.uid)
            }
        }
    }

    suspend fun updateDate(date: Date): Result<Date?>{
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.updateDate(date, it.uid)
            }
        }
    }
}