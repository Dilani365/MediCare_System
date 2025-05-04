package com.example.save.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.save.helpers.NotificationHelper
import java.util.Calendar

//background service

class NotificationService : Service() {
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var alarmManager: AlarmManager

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        scheduleMonthlyReminder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_MONTHLY_REMINDER -> {
                notificationHelper.showMonthlyReminder()
            }
            ACTION_BUDGET_ALERT -> {
                val category = intent.getStringExtra(EXTRA_CATEGORY) ?: return START_NOT_STICKY
                val percentageUsed = intent.getIntExtra(EXTRA_PERCENTAGE_USED, 0)
                notificationHelper.showBudgetAlert(category, percentageUsed)
            }
        }
        return START_NOT_STICKY
    }

    private fun scheduleMonthlyReminder() {
        val intent = Intent(this, NotificationService::class.java).apply {
            action = ACTION_MONTHLY_REMINDER
        }

        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.MONTH, 1)
            }
        }

        val monthInMillis = 30L * 24 * 60 * 60 * 1000 // 30 days in milliseconds
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            monthInMillis,
            pendingIntent
        )
    }

    fun scheduleBudgetAlert(category: String, percentageUsed: Int) {
        val intent = Intent(this, NotificationService::class.java).apply {
            action = ACTION_BUDGET_ALERT
            putExtra(EXTRA_CATEGORY, category)
            putExtra(EXTRA_PERCENTAGE_USED, percentageUsed)
        }

        val pendingIntent = PendingIntent.getService(
            this,
            category.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 1000, // Show immediately
            pendingIntent
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_MONTHLY_REMINDER = "com.example.save.ACTION_MONTHLY_REMINDER"
        const val ACTION_BUDGET_ALERT = "com.example.save.ACTION_BUDGET_ALERT"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_PERCENTAGE_USED = "extra_percentage_used"
    }
} 