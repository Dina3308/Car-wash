package ru.kpfu.itis.domain.interfaces

import ru.kpfu.itis.domain.model.CarWash

interface CarWashesRepository {
    suspend fun getNearbyCarWashes(lat: Double, long: Double): List<CarWash>?
    suspend fun getCarWashes(lat: Double, long: Double): List<CarWash>?
}
