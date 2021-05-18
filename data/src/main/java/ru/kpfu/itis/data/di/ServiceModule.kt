package ru.kpfu.itis.data.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kpfu.itis.data.api.places.PlacesService
import ru.kpfu.itis.data.api.weather.WeatherService
import javax.inject.Named
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun bindPlacesService(@Named(TAG_RETROFIT_PLACES)retrofit: Retrofit): PlacesService = retrofit.create(
        PlacesService::class.java
    )

    @Provides
    @Singleton
    fun bindWeatherService(@Named(TAG_RETROFIT_WEATHER)retrofit: Retrofit): WeatherService = retrofit.create(
        WeatherService::class.java
    )

    companion object {
        private const val TAG_RETROFIT_WEATHER = "tag_retrofit_weather"
        private const val TAG_RETROFIT_PLACES = "tag_retrofit_places"
    }
}
