package com.example.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.utils.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val languageManager: LanguageManager
) : ViewModel() {

    fun changeLanguage(langCode: String) {
        languageManager.setLocale(langCode)
    }

    fun getCurrentLanguage(): String {
        return languageManager.getSavedLanguage()
    }
}
