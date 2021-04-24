package ru.kpfu.itis.domain.interfaces

import ru.kpfu.itis.domain.model.CurrentWeather
import ru.kpfu.itis.domain.model.DailyWeather

interface WeatherRepository {

    suspend fun getWeatherByCoord(lat: Double, lon: Double): CurrentWeather

    suspend fun getForecastsWeather(lat: Double, lon: Double): List<DailyWeather>
}