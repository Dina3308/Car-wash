package ru.kpfu.itis.data.api.places

import com.google.gson.annotations.SerializedName

data class NearbyPlacesResponse(
    @SerializedName("results") val results: List<Place>
)
