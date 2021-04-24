package ru.kpfu.itis.domain.model

data class CurrentWeather(
    val temp: Double,
    val description: String,
    val icon: String,
    val tempMax: Double,
    val tempMin: Double,
    val name: String
)
