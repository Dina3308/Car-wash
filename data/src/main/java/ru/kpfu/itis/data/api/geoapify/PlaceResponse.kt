package ru.kpfu.itis.data.api.geoapify

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
    var lon: Double
)