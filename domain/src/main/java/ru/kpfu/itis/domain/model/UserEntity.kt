package ru.kpfu.itis.domain.model

import java.util.*

data class UserEntity(
    val address: String?,
    val lat: Double?,
    val lon: Double?,
    val date: Date?,
    val levelOfCarPollution: Int?
)
