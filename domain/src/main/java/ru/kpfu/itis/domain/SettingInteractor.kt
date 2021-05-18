package ru.kpfu.itis.domain

import com.google.android.libraries.places.api.model.Place
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository

class SettingInteractor(
    private val fireStoreRepository: FireStoreRepository,
    private val authRepository: AuthRepository
) {

    suspend fun updateCity(place: Place): Result<Place?> {
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.updateCity(place, it.uid)
            }
        }
    }

    suspend fun getCity(): Result<String?> {
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.getUserDocument(it.uid).address
            }
        }
    }
}
