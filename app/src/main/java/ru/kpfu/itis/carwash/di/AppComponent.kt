package ru.kpfu.itis.carwash.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.carwash.auth.di.SignInComponent
import ru.kpfu.itis.carwash.auth.di.SignUpComponent
import ru.kpfu.itis.carwash.map.di.MapsComponent
import ru.kpfu.itis.carwash.profile.di.ProfileComponent
import ru.kpfu.itis.carwash.profile.workManager.di.CarWashWorkerFactory
import ru.kpfu.itis.carwash.profile.workManager.di.CarWashWorkerModule
import ru.kpfu.itis.carwash.setting.di.SettingComponent
import ru.kpfu.itis.carwash.splash_screen.di.SplashComponent
import ru.kpfu.itis.data.di.DbModule
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
        ServiceModule::class,
        CarWashWorkerModule::class,
        DbModule::class
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

    fun homeComponentFactory(): ProfileComponent.Factory

    fun settingComponentFactory(): SettingComponent.Factory

    fun factory(): CarWashWorkerFactory
}
