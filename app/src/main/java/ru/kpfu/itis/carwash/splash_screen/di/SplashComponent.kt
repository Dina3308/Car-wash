package ru.kpfu.itis.carwash.splash_screen.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.di.ScreenScope
import ru.kpfu.itis.carwash.splash_screen.SplashFragment

@Subcomponent(
    modules = [
        SplashModule::class
    ]
)
@ScreenScope
interface SplashComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SplashComponent
    }

    fun inject(splashFragment: SplashFragment)
}
