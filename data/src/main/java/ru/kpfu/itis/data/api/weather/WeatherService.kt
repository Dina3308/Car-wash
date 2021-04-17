package ru.kpfu.itis.data.api.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherByName(
        @Query("q") cityName: String
    ) // : WeatherResponse

    @GET("weather")
    suspend fun getWeatherById(
        @Query("id") cityId: Int
    ) // : WeatherResponse
}
