package ru.kpfu.itis.domain

import android.location.Location

interface LocationRepository {

    suspend fun getUserLocation(): Location
}