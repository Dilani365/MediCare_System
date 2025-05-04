package com.example.save.utils

import android.content.Context
import com.example.save.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupManager(private val context: Context) {
    private val gson = Gson()
    private val backupDir = File(context.getExternalFilesDir(null), "backups")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())

    init {
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
    }

    fun exportData(transactions: List<Transaction>): Boolean {
        return try {
            val timestamp = dateFormat.format(Date())
            val fileName = "backup_$timestamp.json"
            val file = File(backupDir, fileName)
            
            val json = gson.toJson(transactions)
            file.writeText(json)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun importData(fileName: String): List<Transaction>? {
        return try {
            val file = File(backupDir, fileName)
            if (!file.exists()) {
                return null
            }
            
            val json = file.readText()
            val type = object : TypeToken<List<Transaction>>() {}.type
            gson.fromJson<List<Transaction>>(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getBackupFiles(): List<String> {
        return backupDir.listFiles()
            ?.filter { it.name.endsWith(".json") }
            ?.map { it.name }
            ?.sortedByDescending { it }
            ?: emptyList()
    }

    fun deleteBackup(fileName: String): Boolean {
        return try {
            val file = File(backupDir, fileName)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
} 