package ru.kpfu.itis.domain.interfaces

import android.location.Location

interface LocationRepository {

    suspend fun getUserLocation(): Location
}