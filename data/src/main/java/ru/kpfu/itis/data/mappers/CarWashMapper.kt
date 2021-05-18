package ru.kpfu.itis.data.mappers

import ru.kpfu.itis.data.api.places.Place
import ru.kpfu.itis.data.db.entity.CarWashLocal
import ru.kpfu.itis.domain.model.CarWash

fun mapCarWashLocalToCarWash(carWashLocal: CarWashLocal): CarWash {
    return with(carWashLocal) {
        CarWash(title, lat, lon)
    }
}

fun mapPlaceToCarWashLocal(place: Place): CarWashLocal {
    return with(place) {
        CarWashLocal(name, geometry.location.lat, geometry.location.lng)
    }
}
