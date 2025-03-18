package com.example.app.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.app.MainActivity
import com.example.app.ui.viewmodel.SettingsViewModel



@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val languages = listOf("Español" to "", "Inglés" to "en", "Catalá" to "cat")
    var selectedLanguage by remember { mutableStateOf(viewModel.getCurrentLanguage()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Selecciona un idioma", style = MaterialTheme.typography.bodyLarge)

        languages.forEach { (language, code) ->
            Button(
                onClick = {
                    selectedLanguage = code
                    viewModel.changeLanguage(code)

                    // Reiniciar MainActivity para aplicar cambios
                    val intent = Intent(navController.context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    navController.context.startActivity(intent)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = language)
            }
        }
    }
}
