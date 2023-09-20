package ru.kpfu.itis.carwash.profile.model

data class CurrentWeatherDetails(
    val temp: Double,
    val description: String,
    val icon: String,
    val tempMax: Double,
    val tempMin: Double,
    val name: String
)
