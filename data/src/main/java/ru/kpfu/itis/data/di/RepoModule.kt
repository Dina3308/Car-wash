package ru.kpfu.itis.data.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.data.api.places.PlacesService
import ru.kpfu.itis.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.data.repository.CarWashesRepositoryImpl
import ru.kpfu.itis.data.repository.LocationRepositoryImpl
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.interfaces.LocationRepository
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun provideCarWashesRepository(
        placesService: PlacesService
    ): CarWashesRepository {
        return CarWashesRepositoryImpl(placesService)
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
}
