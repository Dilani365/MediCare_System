package com.example.save.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.save.utils.ReminderManager

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminderManager = ReminderManager(context)
        reminderManager.showNotification()
    }
} 