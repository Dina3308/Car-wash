package ru.kpfu.itis.carwash.setting.di

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
import ru.kpfu.itis.carwash.setting.SettingViewModel
import ru.kpfu.itis.domain.SettingInteractor

@Module(includes = [ViewModelModule::class])
class SettingModule {
    @Provides
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    fun provideViewModel(
        interactor: SettingInteractor,
        resourceManager: ResourceManager,
        networkConnectionUtil: NetworkConnectionUtil
    ): ViewModel = SettingViewModel(interactor, resourceManager, networkConnectionUtil)

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): SettingViewModel =
        ViewModelProvider(fragment, viewModelFactory).get(SettingViewModel::class.java)
}
