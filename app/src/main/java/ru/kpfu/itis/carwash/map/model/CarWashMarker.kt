package ru.kpfu.itis.carwash.map.model

import com.google.android.gms.maps.model.LatLng

data class CarWashMarker(
    val title: String,
    val latLng: LatLng
)
