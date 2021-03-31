package ru.kpfu.itis.carwash.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.data.repository.LocationRepositoryImpl
import ru.kpfu.itis.data.api.places.Place
import ru.kpfu.itis.data.api.places.PlacesService

class MapsViewModel(
    private val placesService: PlacesService,
    private val locationRepository: LocationRepositoryImpl
) : ViewModel(){

    private val carWashes: MutableLiveData<Result<List<Place>>> = MutableLiveData()
    private val location: MutableLiveData<Result<Location>> = MutableLiveData()

    fun carWashes(): MutableLiveData<Result<List<Place>>> = carWashes
    fun location(): MutableLiveData<Result<Location>> = location

    fun showNearbyCarWashes(){
        viewModelScope.launch {
            try {
                val userLocation = locationRepository.getUserLocation()
                location.value = Result.success(userLocation)
                carWashes.value = Result.success(placesService.nearbyPlaces(
                    "${userLocation.latitude},${userLocation.longitude}",
                    3000,
                    "car_wash")
                    .results)

            }catch (throwable : Throwable){
                location.value = Result.failure(throwable)
            }
        }
    }
}