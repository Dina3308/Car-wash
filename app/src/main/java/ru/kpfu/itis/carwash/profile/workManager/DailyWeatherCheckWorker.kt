package ru.kpfu.itis.carwash.profile.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.carwash.profile.workManager.di.ChildWorkerFactory
import ru.kpfu.itis.domain.ProfileInteractor
import java.lang.Exception
import javax.inject.Inject

class DailyWeatherCheckWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val interactor: ProfileInteractor,
    private val notification: NotificationUtil
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                if (interactor.showNotification()) {
                    notification.createNotification()
                }
                Result.success()
            } catch (ex: Exception) {
                Result.failure()
            }
        }
    }

    class Factory @Inject constructor(
        private val interactor: ProfileInteractor,
        private val notification: NotificationUtil
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return DailyWeatherCheckWorker(appContext, params, interactor, notification)
        }
    }
}
