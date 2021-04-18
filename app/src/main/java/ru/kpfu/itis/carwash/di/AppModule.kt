package ru.kpfu.itis.carwash.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.FireStoreInteractor
import ru.kpfu.itis.domain.MapInteractor
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.interfaces.LocationRepository
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAuthInteractor(authRepository: AuthRepository): AuthInteractor =
        AuthInteractor(authRepository)

    @Provides
    @Singleton
    fun provideMapInteractor(
        locationRepository: LocationRepository,
        carWashesRepository: CarWashesRepository
    ): MapInteractor = MapInteractor(locationRepository, carWashesRepository)

    @Provides
    @Singleton
    fun provideFireStoreInteractor(
        fireStoreRepository: FireStoreRepository,
        authRepository: AuthRepository
    ): FireStoreInteractor = FireStoreInteractor(fireStoreRepository, authRepository)

    @Provides
    @Singleton
    fun provideFusedClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore
}
