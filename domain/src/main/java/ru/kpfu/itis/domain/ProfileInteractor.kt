package ru.kpfu.itis.domain

import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.interfaces.WeatherRepository
import ru.kpfu.itis.domain.model.CurrentWeather
import ru.kpfu.itis.domain.model.DailyWeather
import ru.kpfu.itis.domain.model.PrecipitationIntensity
import ru.kpfu.itis.domain.model.UserEntity
import java.util.*
import java.util.Calendar.DAY_OF_YEAR

class ProfileInteractor(
    private val authRepository: AuthRepository,
    private val weatherRepository: WeatherRepository,
    private val fireStoreRepository: FireStoreRepository
) {

    companion object {
        private const val LEVEL_OF_CAR_POLLUTION = 7
    }

    suspend fun signOut() = authRepository.signOut()

    suspend fun updateDate(date: Date): Result<Date?> {
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.updateDate(date, it.uid)
            }
        }
    }

    suspend fun updateLevelOfCarPollution(level: Long): Result<Long?> {
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.updateLevelOfCarPollution(level, it.uid)
            }
        }
    }

    suspend fun getUserDocument(): Result<UserEntity?> {
        return runCatching {
            authRepository.getCurrentUser()?.let {
                fireStoreRepository.getUserDocument(it.uid)
            }
        }
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<CurrentWeather> {
        return runCatching {
            weatherRepository.getWeatherByCoord(lat, lon)
        }
    }

    suspend fun getDayOfCarWash(lat: Double, lon: Double): Date? {
        var countOfDryDays = 0
        var numberDay = 1
        var carWashDay: Int? = null
        val date = Calendar.getInstance()

        getForecastsWeather(lat, lon).forEach { elem ->
            if (elem.rain == null && elem.snow == null) {
                countOfDryDays += 1
                countOfDryDays.takeIf { it == 3 }?.let {
                    carWashDay = numberDay - 2
                    return@forEach
                }
            } else {
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

    suspend fun showNotification(): Boolean {
        val user = getUserDocument().getOrNull()
        val lat = user?.lat
        val lon = user?.lon
        if (lat != null && lon != null) {
            getForecastsWeather(lat, lon).apply {
                val rain = this[0].rain
                val snow = this[0].snow
                val level = user.levelOfCarPollution ?: 0
                if (rain != null) {
                    updateLevelOfCarPollution((level + getPrecipitationIntensity(rain)).toLong())
                }
                if (snow != null) {
                    updateLevelOfCarPollution((level + getPrecipitationIntensity(snow)).toLong())
                }
                if (snow == null && rain == null) {
                    updateLevelOfCarPollution((level + 1).toLong())
                }
                if (isNotify(lat, lon, level)) {
                    return true
                }
            }
        }
        return false
    }

    private fun getPrecipitationIntensity(precipitationAmount: Double): Int = when {
        precipitationAmount < PrecipitationIntensity.MODERATE.precipitationAmount -> PrecipitationIntensity.LIGHT.level
        precipitationAmount < PrecipitationIntensity.HEAVY.precipitationAmount -> PrecipitationIntensity.MODERATE.level
        else -> PrecipitationIntensity.HEAVY.level
    }

    private suspend fun getForecastsWeather(lat: Double, lon: Double): List<DailyWeather> {
        return weatherRepository.getForecastsWeather(lat, lon)
    }

    private suspend fun isNotify(lat: Double, lon: Double, level: Int): Boolean {
        getDayOfCarWash(lat, lon)?.also {
            val day = Calendar.getInstance().also { cal ->
                cal.time = it
            }
            if (Calendar.getInstance().get(DAY_OF_YEAR) == day.get(DAY_OF_YEAR) && level > LEVEL_OF_CAR_POLLUTION) {
                return true
            }
        }
        return false
    }
}
