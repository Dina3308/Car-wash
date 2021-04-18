package ru.kpfu.itis.carwash.setting.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.di.ScreenScope
import ru.kpfu.itis.carwash.setting.SettingFragment

@Subcomponent(
    modules = [
        SettingModule::class
    ]
)
@ScreenScope
interface SettingComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SettingComponent
    }

    fun inject(settingFragment: SettingFragment)
}
