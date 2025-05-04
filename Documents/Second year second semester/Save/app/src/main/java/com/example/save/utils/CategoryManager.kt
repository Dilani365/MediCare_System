package com.example.save.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "category_preferences"
        private const val KEY_CUSTOM_CATEGORIES = "custom_categories"
        
        val DEFAULT_CATEGORIES = listOf(
            "Food",
            "Car",
            "Children",
            "Travel",
            "Study",
            "Shopping",
            "Home",
            "Bills",
            "Transport",
            "Entertainment",
            "Medicine"
        )
    }

    fun getAllCategories(): List<String> {
        val customCategories = getCustomCategories()
        return DEFAULT_CATEGORIES + customCategories
    }

    fun addCustomCategory(category: String) {
        val currentCategories = getCustomCategories().toMutableList()
        if (!currentCategories.contains(category)) {
            currentCategories.add(category)
            saveCustomCategories(currentCategories)
        }
    }

    private fun getCustomCategories(): List<String> {
        val json = sharedPreferences.getString(KEY_CUSTOM_CATEGORIES, null)
        return if (json != null) {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveCustomCategories(categories: List<String>) {
        val json = gson.toJson(categories)
        sharedPreferences.edit().putString(KEY_CUSTOM_CATEGORIES, json).apply()
    }
} 