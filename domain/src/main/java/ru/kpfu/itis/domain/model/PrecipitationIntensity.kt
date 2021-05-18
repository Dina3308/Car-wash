package ru.kpfu.itis.domain.model

enum class PrecipitationIntensity(val precipitationAmount: Int, val level: Int) {
    LIGHT(0, 3),
    MODERATE(2, 5),
    HEAVY(8, 7)
}
