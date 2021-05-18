package ru.kpfu.itis.data.api.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherResponse

    @GET("onecall?exclude=hourly,minutely,alerts")
    suspend fun getForecastsWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): ForecastsWeatherResponse
}
