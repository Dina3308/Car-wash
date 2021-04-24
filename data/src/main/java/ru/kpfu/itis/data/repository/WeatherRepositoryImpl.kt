package ru.kpfu.itis.data.repository

import ru.kpfu.itis.data.api.weather.WeatherService
import ru.kpfu.itis.data.mappers.mapDailyToWeatherDaily
import ru.kpfu.itis.data.mappers.mapWeatherResponseToWeatherCurrent
import ru.kpfu.itis.domain.interfaces.WeatherRepository
import ru.kpfu.itis.domain.model.CurrentWeather
import ru.kpfu.itis.domain.model.DailyWeather

class WeatherRepositoryImpl(
    private val weatherService: WeatherService
) : WeatherRepository {

    override suspend fun getWeatherByCoord(lat: Double, lon: Double): CurrentWeather {
        weatherService.getWeatherByCoord(lat, lon).also {
            return mapWeatherResponseToWeatherCurrent(it)
        }
    }

    override suspend fun getForecastsWeather(lat: Double, lon: Double): List<DailyWeather> =
        weatherService.getForecastsWeather(lat, lon).daily.map(::mapDailyToWeatherDaily)
}