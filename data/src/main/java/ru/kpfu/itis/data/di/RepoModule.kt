package ru.kpfu.itis.data.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.data.api.places.PlacesService
import ru.kpfu.itis.data.api.weather.WeatherService
import ru.kpfu.itis.data.db.dao.CarWashDao
import ru.kpfu.itis.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.data.repository.CarWashesRepositoryImpl
import ru.kpfu.itis.data.repository.FireStoreRepositoryImpl
import ru.kpfu.itis.data.repository.LocationRepositoryImpl
import ru.kpfu.itis.data.repository.WeatherRepositoryImpl
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.interfaces.LocationRepository
import ru.kpfu.itis.domain.interfaces.WeatherRepository
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun provideCarWashesRepository(
        placesService: PlacesService,
        carWashDao: CarWashDao
    ): CarWashesRepository {
        return CarWashesRepositoryImpl(placesService, carWashDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        client: FusedLocationProviderClient
    ): LocationRepository {
        return LocationRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideFireStoreRepository(
        db: FirebaseFirestore
    ): FireStoreRepository {
        return FireStoreRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherService: WeatherService
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherService)
    }
}
