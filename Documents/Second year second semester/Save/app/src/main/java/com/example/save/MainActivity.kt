/**
 * MainActivity: Main screen of the Personal Finance Tracker application
 * 
 * This activity handles:
 * 1. Transaction management (add, edit, delete)
 * 2. Budget tracking and visualization
 * 3. Category-wise expense analysis
 * 4. Settings management (currency, budget, reminders)
 * 5. Data backup and restore
 */

package com.example.save

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save.adapters.TransactionAdapter
import com.example.save.databinding.ActivityMainBinding
import com.example.save.databinding.DialogEditTransactionBinding
import com.example.save.models.Transaction
import com.example.save.models.TransactionType
import com.example.save.viewmodels.FinanceViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.util.Date
import java.util.UUID

/**
 * MainActivity manages the main user interface and interactions
 * It implements MVVM architecture using FinanceViewModel for business logic.
 */
class MainActivity : AppCompatActivity() {
    // View binding instance for efficient view access
    private lateinit var binding: ActivityMainBinding
    
    // Adapter for transaction list
    private lateinit var adapter: TransactionAdapter
    
    // ViewModel for managing UI-related data and business logic
    private lateinit var viewModel: FinanceViewModel

    /**
     * Initializes the activity, sets up UI components and observers
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

        requestNotificationPermission()
        setupRecyclerView()
        setupPieChart()
        setupObservers()
        setupClickListeners()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(
            onItemClick = { transaction ->
                showEditTransactionDialog(transaction)
            },
            onDeleteClick = { transaction ->
                showDeleteConfirmationDialog(transaction)
            }
        )
        binding.recyclerViewTransactions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.transactions.observe(this) { transactions ->
            adapter.submitList(transactions)
            updateSummary()
            updatePieChart()
        }

        viewModel.monthlyBudget.observe(this) {
            updateSummary()
        }

        viewModel.currency.observe(this) {
            updateSummary()
        }
    }

    //piechart

    private fun setupPieChart() {
        binding.layoutCategorySummary.pieChart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            legend.isEnabled = true
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT
            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            centerText = "Expenses"
            setCenterTextSize(16f)
            animateY(1000)
        }
    }

    private fun updatePieChart() {
        val categoryExpenses = viewModel.getCategoryWiseExpenses()
        if (categoryExpenses.isEmpty()) {
            binding.layoutCategorySummary.pieChart.visibility = android.view.View.GONE
            return
        }

        binding.layoutCategorySummary.pieChart.visibility = android.view.View.VISIBLE
        val entries = categoryExpenses.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "Expenses by Category").apply {
            colors = listOf(
                Color.rgb(64, 89, 128),
                Color.rgb(149, 165, 124),
                Color.rgb(217, 184, 162),
                Color.rgb(191, 134, 134),
                Color.rgb(179, 48, 80),
                Color.rgb(193, 37, 82),
                Color.rgb(255, 102, 0),
                Color.rgb(245, 199, 0),
                Color.rgb(106, 150, 31),
                Color.rgb(179, 100, 53)
            )
            valueTextSize = 12f
            valueFormatter = PercentFormatter(binding.layoutCategorySummary.pieChart)
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(binding.layoutCategorySummary.pieChart))
            setValueTextSize(11f)
            setValueTextColor(Color.BLACK)
        }

        binding.layoutCategorySummary.pieChart.apply {
            this.data = data
            highlightValues(null)
            invalidate()
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTransaction.setOnClickListener {
            showAddTransactionDialog()
        }

        binding.buttonSetBudget.setOnClickListener {
            showSetBudgetDialog()
        }

        binding.buttonSetCurrency.setOnClickListener {
            showCurrencyDialog()
        }
    }

    private fun showAddTransactionDialog(transaction: Transaction? = null) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_transaction, null)
        val titleEditText = dialogBinding.findViewById<EditText>(R.id.editTextTitle)
        val amountEditText = dialogBinding.findViewById<EditText>(R.id.editTextAmount)
        val categorySpinner = dialogBinding.findViewById<Spinner>(R.id.spinnerCategory)
        val incomeRadioButton = dialogBinding.findViewById<RadioButton>(R.id.radioIncome)

        val categories = (viewModel.categories.value ?: emptyList())
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(if (transaction == null) "Add Transaction" else "Edit Transaction")
            .setView(dialogBinding)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener { dialogInterface ->
            val positiveButton = (dialogInterface as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val title = titleEditText.text.toString()
                val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
                val selectedCategory = categorySpinner.selectedItem.toString()
                
                
                            saveTransaction(title, amount,selectedCategory, incomeRadioButton.isChecked, transaction, dialogInterface)
                        }
                    
               
            
        }

        dialog.show()
    }


    //validation
//Transaction Validation:

    private fun saveTransaction(
        title: String,
        amount: Double,
        category: String,
        isIncome: Boolean,
        existingTransaction: Transaction?,
        dialog: AlertDialog
    ) {
        if (title.isNotEmpty() && amount > 0 && category.isNotEmpty()) {
            val newTransaction = Transaction(
                id = existingTransaction?.id ?: UUID.randomUUID().mostSignificantBits,
                title = title,
                amount = amount,
                category = category,
                date = existingTransaction?.date ?: Date(),
                type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE
            )

            if (existingTransaction == null) {
                viewModel.addTransaction(newTransaction)
            } else {
                viewModel.updateTransaction(newTransaction)
            }
            dialog.dismiss()
        } else {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
        }
    }


    //Category Validation
    private fun showAddCategoryDialog(onCategoryAdded: (String) -> Unit) {
        val editText = EditText(this)
        MaterialAlertDialogBuilder(this)
            .setTitle("Add New Category")
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                val category = editText.text.toString().trim()
                if (category.isNotEmpty()) {
                    onCategoryAdded(category)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteConfirmationDialog(transaction: Transaction) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteTransaction(transaction)
                Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    //Budget Validation

    private fun showSetBudgetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_budget, null)
        val editTextBudget = dialogView.findViewById<TextInputEditText>(R.id.editTextBudget)
        val editTextCurrency = dialogView.findViewById<TextInputEditText>(R.id.editTextCurrency)

        editTextBudget.setText(viewModel.monthlyBudget.value?.toString() ?: "")
        editTextCurrency.setText(viewModel.currency.value ?: "$")

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.set_budget)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val budgetStr = editTextBudget.text.toString()
                val currency = editTextCurrency.text.toString().trim()

                if (budgetStr.isNotEmpty()) {
                    try {
                        val budget = budgetStr.toDouble()
                        viewModel.setMonthlyBudget(budget)
                        if (currency.isNotEmpty()) {
                            viewModel.setCurrency(currency)
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showCurrencyDialog() {
        val currencies = arrayOf("USD", "EUR", "GBP", "JPY", "AUD", "CAD", "LKR")
        val currentCurrency = viewModel.currency.value ?: "USD"
        val currentIndex = currencies.indexOf(currentCurrency)

        MaterialAlertDialogBuilder(this)
            .setTitle("Select Currency")
            .setSingleChoiceItems(currencies, currentIndex) { dialog, which ->
                viewModel.setCurrency(currencies[which])
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showBudgetWarningDialog(spent: Double, budget: Double, remaining: Double) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_budget_warning, null)
        val titleView = dialogView.findViewById<TextView>(R.id.textViewWarningTitle)
        val messageView = dialogView.findViewById<TextView>(R.id.textViewWarningMessage)
        val progressBar = dialogView.findViewById<LinearProgressIndicator>(R.id.progressBarWarning)

        val currency = viewModel.currency.value ?: "USD"
        val spentFormatted = formatAmount(spent, currency)
        val budgetFormatted = formatAmount(budget, currency)
        val remainingFormatted = formatAmount(remaining.coerceAtLeast(0.0), currency)

        val progress = ((spent / budget) * 100).toInt().coerceIn(0, 100)
        progressBar.progress = progress

        if (spent > budget) {
            titleView.text = getString(R.string.warning_title_exceeded)
            messageView.text = getString(R.string.warning_message_exceeded, 
                formatAmount(spent - budget, currency))
            progressBar.setIndicatorColor(resources.getColor(android.R.color.holo_red_dark, theme))
        } else {
            titleView.text = getString(R.string.warning_title_budget)
            messageView.text = getString(R.string.warning_message_budget, 
                spentFormatted, budgetFormatted, remainingFormatted)
            progressBar.setIndicatorColor(
                when {
                    progress >= 90 -> resources.getColor(android.R.color.holo_red_light, theme)
                    progress >= 75 -> resources.getColor(android.R.color.holo_orange_dark, theme)
                    else -> resources.getColor(android.R.color.holo_orange_light, theme)
                }
            )
        }

        MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun updateSummary() {
        val transactions = viewModel.transactions.value ?: emptyList()
        val monthlyBudget = viewModel.monthlyBudget.value ?: 0.0
        val currency = viewModel.currency.value ?: "USD"
        
        val expenses = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
            
        val remaining = monthlyBudget - expenses
        
        binding.textViewBudgetSummary.text = getString(
            R.string.budget_summary,
            formatAmount(monthlyBudget, currency),
            formatAmount(expenses, currency),
            formatAmount(remaining, currency)
        )

        if (monthlyBudget > 0) {
            val progress = ((expenses / monthlyBudget) * 100).toInt().coerceIn(0, 100)
            binding.progressBarBudget.progress = progress

            val color = when {
                progress >= 90 -> getColor(android.R.color.holo_red_light)
                progress >= 75 -> getColor(android.R.color.holo_orange_light)
                else -> getColor(android.R.color.holo_green_light)
            }
            binding.progressBarBudget.setIndicatorColor(color)

            if (expenses >= monthlyBudget) {
                showBudgetWarningDialog(expenses, monthlyBudget, remaining)
            } else if (progress >= 75) {
                showBudgetWarningDialog(expenses, monthlyBudget, remaining)
            }
        } else {
            binding.progressBarBudget.progress = 0
        }
    }

    private fun formatAmount(amount: Double, currency: String): String {
        return when (currency) {
            "USD" -> "$%.2f"
            "EUR" -> "€%.2f"
            "GBP" -> "£%.2f"
            "JPY" -> "¥%.0f"
            "AUD" -> "A$%.2f"
            "CAD" -> "C$%.2f"
            "LKR" -> "Rs %.2f"
            else -> "$%.2f"
        }.format(amount)
    }

    private fun showEditTransactionDialog(transaction: Transaction) {
        val dialogBinding = DialogEditTransactionBinding.inflate(layoutInflater)
        
        val categories = viewModel.categories.value ?: emptyList()
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerCategory.adapter = categoryAdapter
        
        val types = arrayOf("INCOME", "EXPENSE")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerType.adapter = typeAdapter
        
        dialogBinding.editTextAmount.setText(transaction.amount.toString())
        dialogBinding.spinnerCategory.setSelection(categories.indexOf(transaction.category))
        dialogBinding.spinnerType.setSelection(if (transaction.type == TransactionType.INCOME) 0 else 1)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.edit_transaction))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val amount = dialogBinding.editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
                val category = dialogBinding.spinnerCategory.selectedItem.toString()
                val type = if (dialogBinding.spinnerType.selectedItemPosition == 0) TransactionType.INCOME else TransactionType.EXPENSE

                val updatedTransaction = transaction.copy(
                    amount = amount,
                    category = category,
                    type = type
                )

                viewModel.updateTransaction(updatedTransaction)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    //backup
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_backup -> {
                if (viewModel.exportData()) {
                    Toast.makeText(this, "Backup created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Backup failed", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_restore -> {
                showRestoreDialog()
                true
            }
            R.id.action_reminder -> {
                showReminderSettingsDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showRestoreDialog() {
        val backupFiles = viewModel.getBackupFiles()
        if (backupFiles.isEmpty()) {
            Toast.makeText(this, "No backup files found", Toast.LENGTH_SHORT).show()
            return
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Restore Backup")
            .setItems(backupFiles.toTypedArray()) { _, which ->
                val fileName = backupFiles[which]
                if (viewModel.importData(fileName)) {
                    Toast.makeText(this, "Backup restored successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Restore failed", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun showReminderSettingsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reminder_settings, null)
        val switchReminder = dialogView.findViewById<SwitchMaterial>(R.id.switchReminder)
        val timePickerReminder = dialogView.findViewById<TimePicker>(R.id.timePickerReminder)

        switchReminder.isChecked = viewModel.isDailyReminderEnabled()
        val (hour, minute) = viewModel.getReminderTime()
        timePickerReminder.hour = hour
        timePickerReminder.minute = minute

        MaterialAlertDialogBuilder(this)
            .setTitle("Daily Reminder Settings")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                viewModel.setDailyReminderEnabled(switchReminder.isChecked)
                if (switchReminder.isChecked) {
                    viewModel.setReminderTime(timePickerReminder.hour, timePickerReminder.minute)
                    viewModel.scheduleDailyReminder()
                } else {
                    viewModel.cancelDailyReminder()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 123
    }
}