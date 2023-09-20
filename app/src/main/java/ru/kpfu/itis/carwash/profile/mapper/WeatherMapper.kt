package ru.kpfu.itis.carwash.profile.mapper

import ru.kpfu.itis.carwash.profile.model.CurrentWeatherDetails
import ru.kpfu.itis.domain.model.CurrentWeather

fun mapCurrentWeatherToCurrentWeatherDetails(weather: CurrentWeather): CurrentWeatherDetails {
    return with(weather) {
        CurrentWeatherDetails(
            temp,
            description,
            icon,
            tempMax,
            tempMin,
            name
        )
    }
}
