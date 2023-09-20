package ru.kpfu.itis.carwash.map.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.common.ResourceManager
import ru.kpfu.itis.carwash.di.ViewModelKey
import ru.kpfu.itis.carwash.di.ViewModelModule
import ru.kpfu.itis.carwash.map.MapsViewModel
import ru.kpfu.itis.domain.MapInteractor

@Module(includes = [ViewModelModule::class])
class MapsModule {

    @Provides
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    fun provideViewModel(
        interactor: MapInteractor,
        resourceManager: ResourceManager
    ): ViewModel = MapsViewModel(interactor, resourceManager)

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): MapsViewModel =
        ViewModelProvider(fragment, viewModelFactory).get(MapsViewModel::class.java)
}
