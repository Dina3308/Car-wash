package ru.kpfu.itis.data.mappers

import com.google.firebase.firestore.DocumentSnapshot
import ru.kpfu.itis.domain.model.UserEntity

private const val LEVEL_OF_CAR_POLLUTION_FIELD = "levelOfCarPollution"
private const val DATE_FIELD = "date"
private const val LOCATION_FIELD = "location"
private const val ADDRESS_FIELD = "address"

fun mapDocumentSnapShotToUserEntity(document: DocumentSnapshot): UserEntity {
    return with(document) {
        UserEntity(
            getString(ADDRESS_FIELD),
            Pair(getGeoPoint(LOCATION_FIELD)?.latitude, getGeoPoint(LOCATION_FIELD)?.longitude),
            getDate(DATE_FIELD),
            getLong(LEVEL_OF_CAR_POLLUTION_FIELD)?.toInt()
        )
    }
}
