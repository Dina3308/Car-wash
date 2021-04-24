package ru.kpfu.itis.domain

import com.google.firebase.firestore.DocumentSnapshot
import org.joda.time.Days
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.interfaces.WeatherRepository
import ru.kpfu.itis.domain.model.CurrentWeather
import ru.kpfu.itis.domain.model.DailyWeather
import java.util.*

class WeatherInteractor(
    private val weatherRepository: WeatherRepository
) {

    suspend fun getWeatherByCoord(lat: Double, lon: Double): CurrentWeather {
        return weatherRepository.getWeatherByCoord(lat, lon)
    }

    suspend fun getForecastsWeather(lat: Double, lon: Double): List<DailyWeather>{
        return weatherRepository.getForecastsWeather(lat, lon)
    }

    fun getDayOfCarWash(weathers: List<DailyWeather>, document: DocumentSnapshot): Date? {
        var countOfDryDays = 0
        var numberDay = 1
        var carWashDay: Int? = null
        val date= Calendar.getInstance()

        weathers.forEach { elem ->
            if (elem.rain == null && elem.snow == null) {
                countOfDryDays += 1
                countOfDryDays.takeIf{it >= 4}?.let{
                    document.getLong("levelOfCarPollution")?.let {level ->
                        document.getDate("date")?.run {
                            if(((elem.dt * 1000).toLong() - time) / (1000*60*60*24) + level > 10){
                                carWashDay = numberDay - 3
                                return@forEach
                            }
                        }

                    }
                }
            }
            else {
                countOfDryDays = 0
            }
            numberDay += 1
        }

        carWashDay?.let {
            date.add(Calendar.DAY_OF_MONTH, it - 1)
            return date.time
        }

        return null
    }
}