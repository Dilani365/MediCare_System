/**
 * FinanceViewModel: Manages the business logic and data for the finance tracking application
 *
 * This ViewModel handles:
 * 1. Transaction data management
 * 2. Budget calculations and tracking
 * 3. Category-wise expense analysis
 * 4. Data persistence through PreferencesManager
 * 5. Backup and restore operations
 * 6. Notification and reminder management
 */

package com.example.save.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.save.helpers.NotificationHelper
import com.example.save.models.Transaction
import com.example.save.models.TransactionType
import com.example.save.utils.BackupManager
import com.example.save.utils.CategoryManager
import com.example.save.utils.PreferencesManager
import com.example.save.utils.ReminderManager
import java.util.Calendar

/**
 * ViewModel class that manages all finance-related data and operations.
 * Uses LiveData for observable data patterns and maintains data across configuration changes.
 */
class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    // Managers for different functionalities
    private val preferencesManager = PreferencesManager(application)
    private val notificationHelper = NotificationHelper(application)
    private val backupManager = BackupManager(application)
    private val categoryManager = CategoryManager(application)
    private val reminderManager = ReminderManager(application)

    // LiveData for observable data
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _monthlyBudget = MutableLiveData<Double>()
    val monthlyBudget: LiveData<Double> = _monthlyBudget

    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String> = _currency

    private val _categoryBudgets = MutableLiveData<Map<String, Double>>()
    val categoryBudgets: LiveData<Map<String, Double>> = _categoryBudgets

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    /**
     * Initializes the ViewModel by loading saved data
     */
    init {
        loadData()
        loadCategoryBudgets()
        loadCategories()
    }

    /**
     * Loads transaction data, budget, and currency settings from persistent storage
     */
    private fun loadData() {
        _transactions.value = preferencesManager.getTransactions()
        _monthlyBudget.value = preferencesManager.monthlyBudget
        _currency.value = preferencesManager.getCurrency()
    }

    private fun loadCategoryBudgets() {
        _categoryBudgets.value = preferencesManager.getCategoryBudgets()
    }

    private fun loadCategories() {
        _categories.value = categoryManager.getAllCategories()
    }

    fun addTransaction(transaction: Transaction) {
        val currentList = _transactions.value?.toMutableList() ?: mutableListOf()
        currentList.add(transaction)
        _transactions.value = currentList
        preferencesManager.saveTransactions(currentList)
        checkBudgetAlerts(transaction)
    }

    fun updateTransaction(transaction: Transaction) {
        val currentList = _transactions.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            currentList[index] = transaction
            _transactions.value = currentList
            preferencesManager.saveTransactions(currentList)
            checkBudgetAlerts(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        val currentList = _transactions.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == transaction.id }
        _transactions.value = currentList
        preferencesManager.saveTransactions(currentList)
    }

    fun setMonthlyBudget(budget: Double) {
        _monthlyBudget.value = budget
        preferencesManager.monthlyBudget = budget
    }

    fun setCurrency(currency: String) {
        preferencesManager.setCurrency(currency)
        _currency.value = currency
    }

    fun setCategoryBudget(category: String, budget: Double) {
        preferencesManager.setCategoryBudget(category, budget)
        loadCategoryBudgets()
    }

    fun addCustomCategory(category: String) {
        categoryManager.addCustomCategory(category)
        loadCategories()
    }

    private fun checkBudgetAlerts(transaction: Transaction) {
        if (transaction.type == TransactionType.EXPENSE) {
            // Check monthly budget
            val monthlyBudget = _monthlyBudget.value ?: return
            val currentMonthExpenses = getCurrentMonthExpenses()
            val monthlyPercentageUsed = ((currentMonthExpenses / monthlyBudget) * 100).toInt()
            
            if (monthlyPercentageUsed >= 75) {
                notificationHelper.showBudgetAlert("monthly", monthlyPercentageUsed)
            }
            
            // Check category budget
            val categoryBudgets = _categoryBudgets.value ?: return
            val categoryBudget = categoryBudgets[transaction.category] ?: return
            val categoryExpenses = getCategoryExpenses(transaction.category)
            val categoryPercentageUsed = ((categoryExpenses / categoryBudget) * 100).toInt()
            
            if (categoryPercentageUsed >= 75) {
                notificationHelper.showBudgetAlert(transaction.category, categoryPercentageUsed)
            }
        }
    }

    fun getCurrentMonthExpenses(): Double {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        
        return _transactions.value?.filter { transaction ->
            val transactionDate = Calendar.getInstance().apply {
                time = transaction.date
            }
            transactionDate.get(Calendar.MONTH) == currentMonth &&
            transactionDate.get(Calendar.YEAR) == currentYear &&
            transaction.type == TransactionType.EXPENSE
        }?.sumOf { it.amount } ?: 0.0
    }

    private fun getCategoryExpenses(category: String): Double {
        return _transactions.value?.filter { 
            it.category == category && it.type == TransactionType.EXPENSE 
        }?.sumOf { it.amount } ?: 0.0
    }

    fun exportData(): Boolean {
        return _transactions.value?.let { transactions ->
            backupManager.exportData(transactions)
        } ?: false
    }

    fun importData(fileName: String): Boolean {
        val importedTransactions = backupManager.importData(fileName) ?: return false
        val currentList = _transactions.value?.toMutableList() ?: mutableListOf()
        currentList.addAll(importedTransactions)
        _transactions.value = currentList
        preferencesManager.saveTransactions(currentList)
        loadData()
        return true
    }

    fun getBackupFiles(): List<String> {
        return backupManager.getBackupFiles()
    }

    fun deleteBackup(fileName: String): Boolean {
        return backupManager.deleteBackup(fileName)
    }

    fun isDailyReminderEnabled(): Boolean {
        return preferencesManager.isDailyReminderEnabled()
    }

    fun getReminderTime(): Pair<Int, Int> {
        return preferencesManager.getReminderTime()
    }

    fun setDailyReminderEnabled(enabled: Boolean) {
        preferencesManager.setDailyReminderEnabled(enabled)
    }

    fun setReminderTime(hour: Int, minute: Int) {
        preferencesManager.setReminderTime(hour, minute)
    }

    fun scheduleDailyReminder() {
        val (hour, minute) = getReminderTime()
        reminderManager.scheduleReminder(hour, minute)
    }

    fun cancelDailyReminder() {
        reminderManager.cancelReminder()
    }

    /**
     * Calculates category-wise expenses and their percentages
     * @return Map of category names to their total expenses
     */
    fun getCategoryWiseExpenses(): Map<String, Double> {
        val transactions = _transactions.value ?: return emptyMap()
        return transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
    }

    fun getCategoryPercentages(): Map<String, Double> {
        val categoryExpenses = getCategoryWiseExpenses()
        val totalExpenses = categoryExpenses.values.sum()
        return if (totalExpenses > 0) {
            categoryExpenses.mapValues { (_, amount) -> (amount / totalExpenses) * 100 }
        } else {
            emptyMap()
        }
    }
} 