package com.urbantransit.offline.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransitNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_APPROACHING_STOP,
                "Transit Stop Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High-priority alerts when approaching your target transit stop"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
                setShowBadge(true)
            }

            val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            systemNotificationManager.createNotificationChannel(channel)
        }
    }

    fun showStopApproachingNotification(stopId: String, stopName: String, distanceMeters: Int = 500) {
        // Fallback or explicit intent to launch the main app
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_STOP_ID, stopId)
        }

        val pendingIntent = launchIntent?.let {
            PendingIntent.getActivity(
                context,
                stopId.hashCode(),
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_APPROACHING_STOP)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle("Approaching Your Stop!")
            .setContentText("You are within $distanceMeters meters of $stopName. Prepare to alight.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("You are entering the 500-meter proximity zone for $stopName. Check your belongings and prepare for your stop.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .apply {
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
            }
            .build()

        try {
            notificationManager.notify(stopId.hashCode(), notification)
        } catch (securityException: SecurityException) {
            // Handled when POST_NOTIFICATIONS runtime permission has not been granted
        }
    }

    companion object {
        const val CHANNEL_ID_APPROACHING_STOP = "approaching_transit_stop_channel"
        const val EXTRA_STOP_ID = "extra_stop_id"
    }
}
