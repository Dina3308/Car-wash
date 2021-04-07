package ru.kpfu.itis.carwash.auth.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.auth.SignUpFragment
import ru.kpfu.itis.carwash.di.ScreenScope

@Subcomponent(
    modules = [
        SignUpModule::class
    ]
)
@ScreenScope
interface SignUpComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SignUpComponent
    }

    fun inject(SignUpFragment: SignUpFragment)
}
