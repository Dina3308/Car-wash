package ru.kpfu.itis.carwash.profile.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.common.NetworkConnectionUtil
import ru.kpfu.itis.carwash.common.ResourceManager
import ru.kpfu.itis.carwash.di.ViewModelKey
import ru.kpfu.itis.carwash.di.ViewModelModule
import ru.kpfu.itis.carwash.profile.ProfileViewModel
import ru.kpfu.itis.domain.ProfileInteractor

@Module(includes = [ViewModelModule::class])
class ProfileModule {
    @Provides
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun provideViewModel(
        interactor: ProfileInteractor,
        resourceManager: ResourceManager,
        networkConnectionUtil: NetworkConnectionUtil
    ): ViewModel = ProfileViewModel(interactor, resourceManager, networkConnectionUtil)

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): ProfileViewModel =
        ViewModelProvider(fragment, viewModelFactory).get(ProfileViewModel::class.java)
}
