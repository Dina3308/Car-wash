package ru.kpfu.itis.carwash.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.carwash.auth.di.SignInComponent
import ru.kpfu.itis.carwash.auth.di.SignUpComponent
import ru.kpfu.itis.carwash.home.di.HomeComponent
import ru.kpfu.itis.carwash.map.di.MapsComponent
import ru.kpfu.itis.carwash.splash_screen.di.SplashComponent
import ru.kpfu.itis.data.di.NetworkModule
import ru.kpfu.itis.data.di.RepoModule
import ru.kpfu.itis.data.di.ServiceModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepoModule::class,
        ServiceModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Context): Builder

        fun build(): AppComponent
    }

    fun mapsComponentFactory(): MapsComponent.Factory

    fun signUpComponentFactory(): SignUpComponent.Factory

    fun signInComponentFactory(): SignInComponent.Factory

    fun splashComponentFactory(): SplashComponent.Factory

    fun homeComponentFactory(): HomeComponent.Factory
}
