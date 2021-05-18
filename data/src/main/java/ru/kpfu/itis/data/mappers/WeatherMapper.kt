package ru.kpfu.itis.data.mappers

import ru.kpfu.itis.data.api.weather.Daily
import ru.kpfu.itis.data.api.weather.WeatherResponse
import ru.kpfu.itis.domain.model.CurrentWeather
import ru.kpfu.itis.domain.model.DailyWeather

fun mapWeatherResponseToWeatherCurrent(weatherResponse: WeatherResponse): CurrentWeather {
    return with(weatherResponse) {
        CurrentWeather(
            main.temp,
            weather[0].description,
            weather[0].icon,
            main.tempMax,
            main.tempMin,
            name
        )
    }
}

fun mapDailyToWeatherDaily(weather: Daily): DailyWeather {
    return with(weather) {
        DailyWeather(
            dt,
            rain,
            snow,
            temp.max,
            temp.min
        )
    }
}
