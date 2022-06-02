package ru.kpfu.itis.carwash.map.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.carwash.di.ScreenScope
import ru.kpfu.itis.carwash.map.HistoryFragment
import ru.kpfu.itis.carwash.map.MapsFragment

@Subcomponent(
    modules = [
        MapsModule::class
    ]
)
@ScreenScope
interface MapsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): MapsComponent
    }

    fun inject(mapsFragment: MapsFragment)
    fun inject(historyFragment: HistoryFragment)
}
