package com.example.app.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    fun setLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        prefs.edit().putString("My_Lang", langCode).apply()
    }

    fun getSavedLanguage(): String {
        return prefs.getString("My_Lang", "es") ?: "es" // Español por defecto
    }
}
