package ru.kpfu.itis.carwash.splash_screen.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.di.ViewModelKey
import ru.kpfu.itis.carwash.di.ViewModelModule
import ru.kpfu.itis.carwash.splash_screen.SplashViewModel
import ru.kpfu.itis.domain.AuthInteractor

@Module(includes = [ViewModelModule::class])
class SplashModule {

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideViewModel(
        interactor: AuthInteractor
    ): ViewModel = SplashViewModel(interactor)

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): SplashViewModel =
        ViewModelProvider(fragment, viewModelFactory).get(SplashViewModel::class.java)
}
