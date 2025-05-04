package com.example.save.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.save.MainActivity
import com.example.save.R

class NotificationHelper(private val context: Context) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Budget Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for budget alerts and warnings"
                enableVibration(true)
            }

            val reminderChannel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily reminders to record expenses"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(reminderChannel)
        }
    }

    fun showBudgetAlert(category: String, percentageUsed: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val title = when {
            percentageUsed >= 100 -> "Budget Exceeded!"
            percentageUsed >= 90 -> "Budget Alert: Critical!"
            percentageUsed >= 75 -> "Budget Alert: Warning!"
            else -> "Budget Update"
        }

        val message = when {
            percentageUsed >= 100 -> "You have exceeded your $category budget!"
            else -> "You have used $percentageUsed% of your $category budget"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Handle the case where notification permission is not granted
        }
    }

    fun showDailyReminder() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 1, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Record Daily Expenses")
            .setContentText("Don't forget to record today's expenses!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(REMINDER_NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Handle the case where notification permission is not granted
        }
    }

    companion object {
        private const val CHANNEL_ID = "budget_alerts"
        private const val REMINDER_CHANNEL_ID = "daily_reminders"
        private const val NOTIFICATION_ID = 1
        private const val REMINDER_NOTIFICATION_ID = 2
    }
} 