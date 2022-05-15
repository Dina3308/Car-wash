package ru.kpfu.itis.data.api.geoapify

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.data.api.places.NearbyPlacesResponse

interface GeoapifyService {

    @GET("places")
    suspend fun nearbyPlaces(
        @Query("categories") categories: String,
        @Query("filter") filter: String,
    ): PlaceResponse
}