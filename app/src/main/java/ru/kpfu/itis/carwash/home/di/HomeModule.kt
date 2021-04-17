package ru.kpfu.itis.carwash.home.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.di.ViewModelKey
import ru.kpfu.itis.carwash.di.ViewModelModule
import ru.kpfu.itis.carwash.home.HomeViewModel
import ru.kpfu.itis.domain.AuthInteractor

@Module(includes = [ViewModelModule::class])
class HomeModule {
    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun provideViewModel(
        interactor: AuthInteractor
    ): ViewModel = HomeViewModel(interactor)

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): HomeViewModel =
        ViewModelProvider(fragment, viewModelFactory).get(HomeViewModel::class.java)
}
