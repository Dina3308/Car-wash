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
import ru.kpfu.itis.carwash.profile.workManager.NotificationUtil
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.MapInteractor
import ru.kpfu.itis.domain.ProfileInteractor
import ru.kpfu.itis.domain.SettingInteractor
import ru.kpfu.itis.domain.interfaces.AuthRepository
import ru.kpfu.itis.domain.interfaces.CarWashesRepository
import ru.kpfu.itis.domain.interfaces.FireStoreRepository
import ru.kpfu.itis.domain.interfaces.LocationRepository
import ru.kpfu.itis.domain.interfaces.WeatherRepository
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAuthInteractor(
        authRepository: AuthRepository,
        fireStoreRepository: FireStoreRepository
    ): AuthInteractor =
        AuthInteractor(authRepository, fireStoreRepository)

    @Provides
    @Singleton
    fun provideMapInteractor(
        locationRepository: LocationRepository,
        carWashesRepository: CarWashesRepository
    ): MapInteractor = MapInteractor(locationRepository, carWashesRepository)

    @Provides
    @Singleton
    fun provideProfileInteractor(
        authRepository: AuthRepository,
        weatherRepository: WeatherRepository,
        fireStoreRepository: FireStoreRepository
    ): ProfileInteractor = ProfileInteractor(authRepository, weatherRepository, fireStoreRepository)

    @Provides
    @Singleton
    fun provideSettingInteractor(
        fireStoreRepository: FireStoreRepository,
        authRepository: AuthRepository
    ): SettingInteractor = SettingInteractor(fireStoreRepository, authRepository)

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

    @Provides
    @Singleton
    fun provideNotification(context: Context): NotificationUtil = NotificationUtil(context)
}
