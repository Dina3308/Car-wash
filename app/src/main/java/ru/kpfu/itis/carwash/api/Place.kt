package ru.kpfu.itis.carwash.api

import com.google.android.gms.maps.model.LatLng

data class Place(
    val id: String,
    val icon: String,
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
) {
    val latLng: LatLng
        get() = LatLng(lat, lng)
}
