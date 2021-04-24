package ru.kpfu.itis.domain.model

data class DailyWeather(
    val dt: Int,
    val rain: Double?,
    val snow: Double?,
    val tempMax: Double,
    val tempMin: Double
)
