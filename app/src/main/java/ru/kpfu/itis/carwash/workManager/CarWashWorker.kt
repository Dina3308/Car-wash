package ru.kpfu.itis.carwash.workManager

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.carwash.workManager.di.ChildWorkerFactory
import ru.kpfu.itis.domain.FireStoreInteractor
import ru.kpfu.itis.domain.WeatherInteractor
import java.util.*
import javax.inject.Inject

class CarWashWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val fireStoreInteractor: FireStoreInteractor,
    private val weatherInteractor: WeatherInteractor
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            try {
                fireStoreInteractor.getUser().getOrNull()?.let { document ->
                    with(weatherInteractor) {
                        document.getGeoPoint("location")?.let {
                            val list = getForecastsWeather(it.latitude, it.longitude)

                            if (list[0].rain != null || list[0].snow != null) {
                                document.getLong("levelOfCarPollution")?.let { level ->
                                    fireStoreInteractor.updateLevelOfCarPollution(
                                        level + 4
                                    )
                                }
                            }

                            getDayOfCarWash(list, document)
                        }
                    }
                }

                Result.success()

            } catch (throwable: Throwable) {
                 Result.failure()
            }
        }
    }

    class Factory @Inject constructor(
        private val fireStoreInteractor: FireStoreInteractor,
        private val weatherInteractor: WeatherInteractor
    ): ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return CarWashWorker(appContext, params, fireStoreInteractor, weatherInteractor)
        }
    }
}