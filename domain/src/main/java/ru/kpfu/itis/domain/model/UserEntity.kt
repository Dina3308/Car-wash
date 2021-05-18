package ru.kpfu.itis.domain.model

import java.util.*

data class UserEntity(
    val address: String?,
    val location: Pair<Double?, Double?>,
    val date: Date?,
    val levelOfCarPollution: Int?
)
