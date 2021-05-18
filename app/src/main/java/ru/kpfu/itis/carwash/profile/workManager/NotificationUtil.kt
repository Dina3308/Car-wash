package ru.kpfu.itis.carwash.profile.workManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.kpfu.itis.carwash.MainActivity
import ru.kpfu.itis.carwash.R
import javax.inject.Inject

class NotificationUtil @Inject constructor(private val context: Context) {

    private val NOTIFICATION_CHANNEL_ID = "101"

    fun createNotification(): Notification? {
        val channel = getNotificationChannel(context)
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channel?.id ?: "")
        } else {
            NotificationCompat.Builder(context)
        }

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.car_wash_logo)
            .setContentText(context.getString(R.string.yes_car_wash))
            .setContentIntent(contentIntent())

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
            notify(5, builder.build())
        }
        return builder.build()
    }

    private fun getNotificationChannel(context: Context): NotificationChannel? {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) ?: run {

                val new = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "channel(1)",
                    NotificationManager.IMPORTANCE_HIGH

                )
                notificationManager.createNotificationChannel(new)
                new
            }
        } else null
    }

    private fun contentIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}
