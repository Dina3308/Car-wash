package ru.kpfu.itis.data.mappers

import ru.kpfu.itis.data.api.places.Place
import ru.kpfu.itis.domain.model.CarWash

fun mapPlaceToCarWash(place: Place): CarWash {
    return with(place) {
        CarWash(id, name, geometry.location.lat, geometry.location.lng)
    }
}
