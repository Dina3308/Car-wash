package ru.kpfu.itis.carwash.map.model

import java.io.Serializable

data class CarWash(
    val name: String,
    val phone: String,
    val address: String,
) : Serializable
