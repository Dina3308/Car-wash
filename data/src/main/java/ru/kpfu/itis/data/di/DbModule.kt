package ru.kpfu.itis.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.data.db.AppDatabase
import ru.kpfu.itis.data.db.dao.CarWashDao
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): AppDatabase = AppDatabase(context)

    @Provides
    @Singleton
    fun provideCarWashDao(db: AppDatabase): CarWashDao = db.carWashDao()
}
