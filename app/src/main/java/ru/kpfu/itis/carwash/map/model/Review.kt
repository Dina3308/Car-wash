package ru.kpfu.itis.carwash.map.model

data class Review(
    val fullName: String,
    val review: String,
    val rating: Int,
    val time: String
)
