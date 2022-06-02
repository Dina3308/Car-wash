package ru.kpfu.itis.data.repository

import ru.kpfu.itis.data.api.geoapify.GeoapifyService
import ru.kpfu.itis.data.api.geoapify.PlaceResponse
import ru.kpfu.itis.data.api.places.PlacesService
import ru.kpfu.itis.data.db.dao.CarWashDao
import ru.kpfu.itis.data.mappers.mapCarWashLocalToCarWash
import ru.kpfu.itis.data.mappers.mapPlaceResponseToCarWashLocal
import ru.kpfu.itis.data.mappers.mapPlaceToCarWashLocal
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.model.CarWash
import java.lang.Exception

class CarWashesRepositoryImpl(
    private val placesService: PlacesService,
    private val carWashDao: CarWashDao,
    private val geoapifyService: GeoapifyService
) : CarWashesRepository {

    companion object {
        private const val PLACE_TYPE = "car_wash"
        private const val RADIUS_IN_METERS = 3000
    }

    override suspend fun getNearbyCarWashes(lat: Double, long: Double): List<CarWash>? {
        return try {
            val carWashes = placesService.nearbyPlaces(
                "$lat,$long",
                RADIUS_IN_METERS,
                PLACE_TYPE
            ).results.map(::mapPlaceToCarWashLocal)
            carWashDao.updateCarWashes(carWashes)
            carWashes.map(::mapCarWashLocalToCarWash)
        } catch (ex: Exception) {
            carWashDao.getCarWashes()?.map(::mapCarWashLocalToCarWash)
        }
    }

    override suspend fun getCarWashes(lat: Double, long: Double): List<CarWash>? {
        return try {
            val carWashes = geoapifyService.nearbyPlaces(
                "service.vehicle.car_wash",
                "circle:$long,$lat,$RADIUS_IN_METERS",
            ).features.map(::mapPlaceResponseToCarWashLocal)
            println(carWashes.toString())
            carWashDao.updateCarWashes(carWashes)
            carWashes.map(::mapCarWashLocalToCarWash)
        } catch (ex: Exception) {
            carWashDao.getCarWashes()?.map(::mapCarWashLocalToCarWash)
        }
    }
}
