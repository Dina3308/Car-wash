package ru.kpfu.itis.carwash.profile.model

import java.util.*

data class UserProfile(
    val address: String?,
    val location: Pair<Double?, Double?>,
    val date: Date?,
    val levelOfCarPollution: Int?
)
