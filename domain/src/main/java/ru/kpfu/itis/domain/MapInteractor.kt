package ru.kpfu.itis.domain

import android.location.Location
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.interfaces.LocationRepository
import ru.kpfu.itis.domain.model.CarWash

class MapInteractor(
    private val locationRepository: LocationRepository,
    private val carWashesRepository: CarWashesRepository
) {
    suspend fun getUserLocation(): Result<Location> {
        return runCatching {
            locationRepository.getUserLocation()
        }
    }

    suspend fun getNearbyCarWashes(myLocation: Location): List<CarWash>? {
        return carWashesRepository.getCarWashes(myLocation.latitude, myLocation.longitude)
    }
}
