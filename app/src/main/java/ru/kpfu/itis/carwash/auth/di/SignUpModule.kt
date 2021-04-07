package ru.kpfu.itis.carwash.auth.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.auth.AuthViewModel
import ru.kpfu.itis.carwash.di.ViewModelKey
import ru.kpfu.itis.carwash.di.ViewModelModule
import ru.kpfu.itis.carwash.map.MapsViewModel
import ru.kpfu.itis.domain.AuthInteractor

@Module(includes = [ViewModelModule::class])
class SignUpModule {

    @Provides
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    fun provideViewModel(
        interactor: AuthInteractor
    ): ViewModel = AuthViewModel(interactor)

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): AuthViewModel =
        ViewModelProvider(fragment, viewModelFactory).get(AuthViewModel::class.java)
}
