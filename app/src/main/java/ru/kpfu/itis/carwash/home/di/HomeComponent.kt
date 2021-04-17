package ru.kpfu.itis.carwash.home.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.di.ScreenScope
import ru.kpfu.itis.carwash.home.HomeFragment

@Subcomponent(
    modules = [
        HomeModule::class
    ]
)
@ScreenScope
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): HomeComponent
    }

    fun inject(homeFragment: HomeFragment)
}
