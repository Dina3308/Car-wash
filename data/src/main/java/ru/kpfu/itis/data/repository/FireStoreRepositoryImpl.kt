package ru.kpfu.itis.data.repository

import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FireStoreRepositoryImpl(
    private val db: FirebaseFirestore
): FireStoreRepository {

    override suspend fun addCity(place: Place, userId: String) : Boolean = suspendCancellableCoroutine { continuation ->

        val city = hashMapOf(
            "address" to place.address,
            "location" to place.latLng,
            "date" to null
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

}