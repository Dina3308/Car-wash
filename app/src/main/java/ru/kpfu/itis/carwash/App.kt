package ru.kpfu.itis.carwash

import android.app.Application
import ru.kpfu.itis.carwash.di.AppComponent
import ru.kpfu.itis.carwash.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}
