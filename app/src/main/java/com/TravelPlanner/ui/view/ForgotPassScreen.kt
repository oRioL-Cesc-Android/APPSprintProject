package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.TravelPlanner.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    // STRINGS
    val successMessage = stringResource(R.string.correo_enviado)
    val errorMessagePrefix = stringResource(R.string.error_enviar_correo)
    val emptyEmailMessage = stringResource(R.string.email_vacio)
    val recuperarContraseña = stringResource(R.string.recuperar_contraseña)
    val ingresaCorreoText = stringResource(R.string.ingresa_correo_para_recuperar)
    val correoElectronicoLabel = stringResource(R.string.correo_electronico)
    val enviarCorreoText = stringResource(R.string.enviar_correo)
    val recuperacionTitle = stringResource(R.string.recuperacion_contraseña)
    val okText = stringResource(R.string.ok)
    val iconoAtrasDesc = stringResource(R.string.icono_atras)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recuperarContraseña) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = iconoAtrasDesc)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = ingresaCorreoText,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(correoElectronicoLabel) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (email.text.isBlank()) {
                        message = emptyEmailMessage
                        showAlert = true
                    } else {
                        auth.sendPasswordResetEmail(email.text)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    message = successMessage
                                } else {
                                    message = errorMessagePrefix + (task.exception?.localizedMessage ?: "")
                                }
                                showAlert = true
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(enviarCorreoText)
            }
        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(recuperacionTitle) },
                text = { Text(message) },
                confirmButton = {
                    Button(onClick = {
                        showAlert = false
                        navController.popBackStack()
                    }) {
                        Text(okText)
                    }
                }
            )
        }
    }
}
