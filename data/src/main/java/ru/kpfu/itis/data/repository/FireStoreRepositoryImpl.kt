package ru.kpfu.itis.data.repository

import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kpfu.itis.data.mappers.mapDocumentSnapShotToUserEntity
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.model.UserEntity
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FireStoreRepositoryImpl(
    private val db: FirebaseFirestore
) : FireStoreRepository {

    companion object {
        private const val COLLECTION_PATH = "users"
        private const val LEVEL_OF_CAR_POLLUTION_FIELD = "levelOfCarPollution"
        private const val DATE_FIELD = "date"
        private const val LOCATION_FIELD = "location"
        private const val ADDRESS_FIELD = "address"
    }

    override suspend fun addUser(place: Place, userId: String): Boolean = suspendCancellableCoroutine { continuation ->

        val city = hashMapOf(
            ADDRESS_FIELD to place.address,
            LOCATION_FIELD to place.latLng?.let { GeoPoint(it.latitude, it.longitude) },
            DATE_FIELD to null,
            LEVEL_OF_CAR_POLLUTION_FIELD to 0
        )

        db.collection(COLLECTION_PATH).document(userId)
            .set(city)
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun updateCity(place: Place, userId: String): Place = suspendCancellableCoroutine { continuation ->
        db.collection(COLLECTION_PATH)
            .document(userId)
            .update(
                mapOf(
                    ADDRESS_FIELD to place.address,
                    LOCATION_FIELD to place.latLng?.let { GeoPoint(it.latitude, it.longitude) }
                )
            )
            .addOnSuccessListener {
                continuation.resume(place)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getUserDocument(userId: String): UserEntity = suspendCancellableCoroutine { continuation ->
        db.collection(COLLECTION_PATH)
            .document(userId)
            .get()
            .addOnSuccessListener {
                it?.run {
                    continuation.resume(mapDocumentSnapShotToUserEntity(this))
                }
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun updateDate(date: Date, userId: String): Date = suspendCancellableCoroutine { continuation ->
        db.collection(COLLECTION_PATH)
            .document(userId)
            .update(
                mapOf(
                    DATE_FIELD to date
                )
            )
            .addOnSuccessListener {
                continuation.resume(date)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun updateLevelOfCarPollution(level: Long, userId: String): Long = suspendCancellableCoroutine { continuation ->
        db.collection(COLLECTION_PATH)
            .document(userId)
            .update(
                mapOf(
                    LEVEL_OF_CAR_POLLUTION_FIELD to level
                )
            )
            .addOnSuccessListener {
                continuation.resume(level)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }
}
