package ru.kpfu.itis.carwash.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.map.model.CarWashMarker
import ru.kpfu.itis.domain.MapInteractor
import ru.kpfu.itis.domain.model.CarWash

class MapsViewModel(
    private val mapInteractor: MapInteractor
) : ViewModel() {

    private val carWashes: MutableLiveData<Result<List<CarWashMarker>>> = MutableLiveData()
    private val location: MutableLiveData<Result<Location>> = MutableLiveData()

    fun carWashes(): MutableLiveData<Result<List<CarWashMarker>>> = carWashes
    fun location(): MutableLiveData<Result<Location>> = location

    fun showNearbyCarWashes() {
        viewModelScope.launch {
            try {
                mapInteractor.getUserLocation().also {
                    location.value = Result.success(it)
                    carWashes.value = Result.success(
                        mapInteractor.getNearbyCarWashes(it)
                            .map(::mapCarWashToCarWashMarker)
                    )
                }
            } catch (throwable: Throwable) {
                location.value = Result.failure(throwable)
            }
        }
    }

    private fun mapCarWashToCarWashMarker(carWash: CarWash): CarWashMarker {
        return with(carWash) {
            CarWashMarker(name, LatLng(lat, lng))
        }
    }
}
