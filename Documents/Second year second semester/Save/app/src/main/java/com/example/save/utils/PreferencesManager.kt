/**
 * PreferencesManager: Handles all data persistence operations using SharedPreferences
 *
 * This class manages:
 * 1. Transaction data storage and retrieval
 * 2. User preferences (currency, budget settings)
 * 3. Category budgets
 * 4. Reminder settings
 * 
 * Uses SharedPreferences for persistent storage and Gson for JSON serialization
 */

package com.example.save.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.save.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manages persistent storage of application data using SharedPreferences
 * Provides type-safe access to stored data with proper error handling
 */
class PreferencesManager(context: Context) {
    // SharedPreferences instance for data storage
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    // Gson instance for JSON serialization
    private val gson = Gson()

    /**
     * Currency management functions
     */
    fun setCurrency(currency: String) {
        prefs.edit().putString(KEY_CURRENCY, currency).apply()
    }

    fun getCurrency(): String {
        return prefs.getString(KEY_CURRENCY, "USD") ?: "USD"
    }

    /**
     * Transaction management functions
     * Uses Gson for serialization/deserialization of transaction list
     */
    fun saveTransactions(transactions: List<Transaction>) {
        val json = gson.toJson(transactions)
        prefs.edit().putString(KEY_TRANSACTIONS, json).apply()
    }

    fun getTransactions(): List<Transaction> {
        val json = prefs.getString(KEY_TRANSACTIONS, null)
        return if (json != null) {
            val type = object : TypeToken<List<Transaction>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    /**
     * Budget management
     * Handles both monthly budget and category-specific budgets
     */
    var monthlyBudget: Double
        get() = prefs.getFloat(KEY_MONTHLY_BUDGET, 0f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MONTHLY_BUDGET, value.toFloat()).apply()

    fun setCategoryBudget(category: String, budget: Double) {
        val budgets = getCategoryBudgets().toMutableMap()
        budgets[category] = budget
        val json = gson.toJson(budgets)
        prefs.edit().putString(KEY_CATEGORY_BUDGETS, json).apply()
    }

    fun getCategoryBudgets(): Map<String, Double> {
        val json = prefs.getString(KEY_CATEGORY_BUDGETS, null)
        return if (json != null) {
            val type = object : TypeToken<Map<String, Double>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyMap()
        }
    }

    /**
     * Reminder settings management
     */
    fun setDailyReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DAILY_REMINDER, enabled).apply()
    }

    fun isDailyReminderEnabled(): Boolean {
        return prefs.getBoolean(KEY_DAILY_REMINDER, false)
    }

    fun setReminderTime(hour: Int, minute: Int) {
        prefs.edit()
            .putInt(KEY_REMINDER_HOUR, hour)
            .putInt(KEY_REMINDER_MINUTE, minute)
            .apply()
    }

    fun getReminderTime(): Pair<Int, Int> {
        val hour = prefs.getInt(KEY_REMINDER_HOUR, 20) // Default 8 PM
        val minute = prefs.getInt(KEY_REMINDER_MINUTE, 0)
        return Pair(hour, minute)
    }

    /**
     * Constants for SharedPreferences keys
     */
    companion object {
        const val PREFS_NAME = "SavePrefs"
        const val KEY_TRANSACTIONS = "transactions"
        const val KEY_MONTHLY_BUDGET = "monthly_budget"
        const val KEY_CATEGORY_BUDGETS = "category_budgets"
        const val KEY_CURRENCY = "currency"
        const val KEY_DAILY_REMINDER = "daily_reminder"
        const val KEY_REMINDER_HOUR = "reminder_hour"
        const val KEY_REMINDER_MINUTE = "reminder_minute"
    }
}