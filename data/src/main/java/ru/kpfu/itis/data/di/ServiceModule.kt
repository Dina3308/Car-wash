package ru.kpfu.itis.data.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kpfu.itis.data.api.places.PlacesService
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun bindPlacesService(retrofit: Retrofit): PlacesService = retrofit.create(
        PlacesService::class.java
    )
}
