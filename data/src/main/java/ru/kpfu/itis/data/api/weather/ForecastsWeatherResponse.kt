package ru.kpfu.itis.data.api.weather

import com.google.gson.annotations.SerializedName

data class ForecastsWeatherResponse(
    @SerializedName("daily")
    var daily: List<Daily>
)

data class Daily(
    @SerializedName("dt")
    var dt: Int,
    @SerializedName("pop")
    var pop: Double,
    @SerializedName("rain")
    var rain: Double?,
    @SerializedName("snow")
    var snow: Double?,
    @SerializedName("temp")
    var temp: Temp
)

data class Temp(
    @SerializedName("min")
    val min : Double,
    @SerializedName("max")
    val max : Double
)
