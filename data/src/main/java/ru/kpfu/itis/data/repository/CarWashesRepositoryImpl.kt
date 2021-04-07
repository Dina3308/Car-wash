package ru.kpfu.itis.data.repository

import ru.kpfu.itis.data.api.places.PlacesService
import ru.kpfu.itis.data.mappers.mapPlaceToCarWash
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.model.CarWash

class CarWashesRepositoryImpl(
    private val placesService: PlacesService,
) : CarWashesRepository {

    override suspend fun getNearbyCarWashes(lat: Double, long: Double): List<CarWash> =
        placesService.nearbyPlaces(
            "$lat,$long",
            3000,
            "car-wash"
        ).results.map(::mapPlaceToCarWash)
}
