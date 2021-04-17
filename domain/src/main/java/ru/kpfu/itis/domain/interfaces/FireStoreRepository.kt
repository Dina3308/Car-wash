package ru.kpfu.itis.domain.interfaces

import com.google.android.libraries.places.api.model.Place

interface FireStoreRepository {

    suspend fun addCity(place: Place, userId:String): Boolean

}