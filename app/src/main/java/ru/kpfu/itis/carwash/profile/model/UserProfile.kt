package ru.kpfu.itis.carwash.profile.model

import java.util.*

data class UserProfile(
    val address: String,
    val lat: Double,
    val lon: Double,
    val date: String,
    val levelOfCarPollution: Float
)
