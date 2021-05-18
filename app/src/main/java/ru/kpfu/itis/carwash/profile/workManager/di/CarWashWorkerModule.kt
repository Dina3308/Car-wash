package ru.kpfu.itis.carwash.profile.workManager.di

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.profile.workManager.DailyWeatherCheckWorker

@Module
interface CarWashWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(DailyWeatherCheckWorker::class)
    fun bindCarWashWorker(worker: DailyWeatherCheckWorker.Factory): ChildWorkerFactory

    @Binds
    fun bindWorkManagerFactory(factory: CarWashWorkerFactory): WorkerFactory
}
