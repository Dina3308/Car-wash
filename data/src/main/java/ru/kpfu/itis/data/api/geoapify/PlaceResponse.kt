package ru.kpfu.itis.data.api.geoapify

import com.google.android.libraries.places.api.model.OpeningHours
import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("features")
    var features: List<Feature>
)

data class Feature(
    @SerializedName("properties")
    var properties: Properties,
)

data class Properties(
    @SerializedName("name")
    var name: String?,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lon")
    var lon: Double,
    @SerializedName("formatted")
    var address: String,
    @SerializedName("opening_hours")
    var openingHours: String,
    @SerializedName("contact")
    var contact: Contact?,
)

data class Contact(
    @SerializedName("phone")
    var phone: String?,
)