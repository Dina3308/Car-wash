package ru.kpfu.itis.carwash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kpfu.itis.carwash.map.MapsViewModel
import ru.kpfu.itis.data.LocationRepositoryImpl
import ru.kpfu.itis.data.api.places.PlacesService

class ViewModelFactory(
    private val placesService: PlacesService,
    private val locationRepository: LocationRepositoryImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(placesService, locationRepository) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
}