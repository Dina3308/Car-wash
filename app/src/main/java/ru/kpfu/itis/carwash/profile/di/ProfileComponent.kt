package ru.kpfu.itis.carwash.profile.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.di.ScreenScope
import ru.kpfu.itis.carwash.profile.ProfileFragment

@Subcomponent(
    modules = [
        ProfileModule::class
    ]
)
@ScreenScope
interface ProfileComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): ProfileComponent
    }

    fun inject(homeFragment: ProfileFragment)
}
