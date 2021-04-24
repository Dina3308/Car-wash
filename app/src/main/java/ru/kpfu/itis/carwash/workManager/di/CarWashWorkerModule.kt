package ru.kpfu.itis.carwash.workManager.di

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kpfu.itis.carwash.workManager.CarWashWorker

@Module
interface CarWashWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CarWashWorker::class)
    fun bindMCarWashWorker(worker: CarWashWorker.Factory): ChildWorkerFactory

    @Binds
    fun bindWorkManagerFactory(factory: CarWashWorkerFactory): WorkerFactory
}