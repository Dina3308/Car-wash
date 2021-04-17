package ru.kpfu.itis.domain

import com.google.android.libraries.places.api.model.Place
import ru.kpfu.itis.domain.interfaces.FireStoreRepository

class FireStoreInteractor(
    private val fireStoreRepository: FireStoreRepository,
) {
    suspend fun addUser(place: Place, userId: String): Result<Boolean>{
        return runCatching{
            fireStoreRepository.addCity(place, userId)
        }
    }
}