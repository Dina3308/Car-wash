package ru.kpfu.itis.carwash.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.common.Event
import ru.kpfu.itis.carwash.common.ResourceManager
import ru.kpfu.itis.carwash.map.model.CarWashMarker
import ru.kpfu.itis.data.api.geoapify.GeoapifyService
import ru.kpfu.itis.data.api.geoapify.PlaceResponse
import ru.kpfu.itis.domain.MapInteractor
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.model.CarWash
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val interactor: MapInteractor,
    private val geoapifyService: GeoapifyService,
) : ViewModel() {

    private val carWashes: MutableLiveData<List<CarWashMarker>> = MutableLiveData()
    private val location: MutableLiveData<Location> = MutableLiveData()
    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val showErrorEvent: MutableLiveData<Event<String>> = MutableLiveData()
    private val carWash: MutableLiveData<ru.kpfu.itis.carwash.map.model.CarWash> = MutableLiveData()

    fun carWashes(): MutableLiveData<List<CarWashMarker>> = carWashes
    fun location(): MutableLiveData<Location> = location
    fun progress(): LiveData<Boolean> = progress
    fun showErrorEvent(): LiveData<Event<String>> = showErrorEvent
    fun carWash(): LiveData<ru.kpfu.itis.carwash.map.model.CarWash> = carWash

    fun showNearbyCarWashes() {
        viewModelScope.launch {
            progress.value = true
            val userLocation = interactor.getUserLocation()
            if (userLocation.isSuccess) {
                userLocation.getOrNull()?.let {
                    location.value = it
                    carWashes.value = interactor.getNearbyCarWashes(it)?.map(::mapCarWashToCarWashMarker)
                }
            }
            progress.value = false
        }
    }

    fun getCarWash(lat: Double, lon: Double): Boolean {
        viewModelScope.launch {
            progress.value = true
            val carWashDetails = geoapifyService.getPlaceDetails(lat, lon)
            carWash.value = mapPlacesResponseToCarWash(carWashDetails)
            progress.value = false
            println(carWash.value.toString())
        }
        return true
    }

    private fun mapCarWashToCarWashMarker(carWash: CarWash): CarWashMarker {
        return with(carWash) {
            CarWashMarker(name, LatLng(lat, lon))
        }
    }

    private fun mapPlacesResponseToCarWash(placeResponse: PlaceResponse): ru.kpfu.itis.carwash.map.model.CarWash {
        return ru.kpfu.itis.carwash.map.model.CarWash(
            name = placeResponse.features[0].properties.name ?: "Авто-мойка",
            address = placeResponse.features[0].properties.address,
            phone = placeResponse.features[0].properties.contact?.phone ?: "+79 393 955 524"
        )
    }
}
