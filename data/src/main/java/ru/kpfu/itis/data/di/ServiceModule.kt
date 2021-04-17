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
    fun bindPlacesService(@Named("tag_retrofit_places")retrofit: Retrofit): PlacesService = retrofit.create(
        PlacesService::class.java
    )

    @Provides
    @Singleton
    fun bindWeatherService(@Named("tag_retrofit_weather")retrofit: Retrofit): WeatherService = retrofit.create(
        WeatherService::class.java
    )
}
