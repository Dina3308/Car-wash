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
import ru.kpfu.itis.domain.MapInteractor
import ru.kpfu.itis.domain.model.CarWash
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val interactor: MapInteractor
) : ViewModel() {

    private val carWashes: MutableLiveData<List<CarWashMarker>> = MutableLiveData()
    private val location: MutableLiveData<Location> = MutableLiveData()
    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val showErrorEvent: MutableLiveData<Event<String>> = MutableLiveData()

    fun carWashes(): MutableLiveData<List<CarWashMarker>> = carWashes
    fun location(): MutableLiveData<Location> = location
    fun progress(): LiveData<Boolean> = progress
    fun showErrorEvent(): LiveData<Event<String>> = showErrorEvent

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

    private fun mapCarWashToCarWashMarker(carWash: CarWash): CarWashMarker {
        return with(carWash) {
            CarWashMarker(name, LatLng(lat, lon))
        }
    }
}
