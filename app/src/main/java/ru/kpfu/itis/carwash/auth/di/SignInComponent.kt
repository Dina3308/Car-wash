package ru.kpfu.itis.carwash.auth.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.auth.SignInFragment
import ru.kpfu.itis.carwash.di.ScreenScope

@Subcomponent(
    modules = [
        AuthModule::class
    ]
)
@ScreenScope
interface SignInComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SignInComponent
    }

    fun inject(fragment: SignInFragment)
}
