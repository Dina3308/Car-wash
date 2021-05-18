package ru.kpfu.itis.carwash

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import ru.kpfu.itis.carwash.di.AppComponent
import ru.kpfu.itis.carwash.di.DaggerAppComponent

class App : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        WorkManager.initialize(this, workManagerConfiguration)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(appComponent.factory())
            .build()
    }
}
