package ru.kpfu.itis.data.repository

import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FireStoreRepositoryImpl(
    private val db: FirebaseFirestore
): FireStoreRepository {

    override suspend fun addUser(place: Place, userId: String) : Boolean = suspendCancellableCoroutine { continuation ->

        val city = hashMapOf(
            "address" to place.address,
            "location" to place.latLng?.let { GeoPoint(it.latitude, it.longitude)},
            "date" to null,
            "levelOfCarPollution" to 0
        )

        db.collection("users").document(userId)
            .set(city)
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun updateCity(place: Place, userId: String): Place = suspendCancellableCoroutine { continuation ->
        db.collection("users")
            .document(userId)
            .update(mapOf(
                "address" to place.address,
                "location" to place.latLng?.let { GeoPoint(it.latitude, it.longitude)}
            ))
            .addOnSuccessListener {
                continuation.resume(place)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getUser(userId: String): DocumentSnapshot = suspendCancellableCoroutine { continuation ->
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                it?.run {
                    continuation.resume(this)
                }
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun updateDate(date: Date, userId: String): Date = suspendCancellableCoroutine { continuation ->
        db.collection("users")
            .document(userId)
            .update(mapOf(
                "date" to date
            ))
            .addOnSuccessListener {
                continuation.resume(date)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun updateLevelOfCarPollution(level: Long, userId: String): Long = suspendCancellableCoroutine { continuation ->
        db.collection("users")
            .document(userId)
            .update(
                mapOf(
                    "levelOfCarPollution" to level
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