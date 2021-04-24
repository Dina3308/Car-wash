package ru.kpfu.itis.domain.interfaces

import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

interface FireStoreRepository {

    suspend fun addUser(place: Place, userId:String): Boolean

    suspend fun updateCity(place: Place, userId:String): Place

    suspend fun getUser(userId:String): DocumentSnapshot

    suspend fun updateDate(date: Date, userId: String): Date

    suspend fun updateLevelOfCarPollution(level: Long, userId:String): Long
}