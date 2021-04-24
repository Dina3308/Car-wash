package ru.kpfu.itis.carwash.profile.model

data class DailyWeatherDetails(
    val dt: Int,
    val rain: Double?,
    val snow: Double?,
    val tempMax: Double,
    val tempMin: Double
)
