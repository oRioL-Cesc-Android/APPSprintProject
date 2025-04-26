package com.TravelPlanner.ui.view

import android.util.Log
import androidx.compose.foundation.layout.*
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.TravelPlanner.R
import com.TravelPlanner.utils.LoginUtils
import androidx.compose.ui.res.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    val loginUtils = LoginUtils()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // NUEVO: Traducciones necesarias
    val loginTitle = stringResource(R.string.Login)
    val usuarioLabel = stringResource(R.string.usuario)
    val contraseñaLabel = stringResource(R.string.contraseña)
    val noTienesCuenta = stringResource(R.string.NoTienesCuenta_Regístrate)
    val olvidasteContraseña = stringResource(R.string.olvidaste_contraseña)
    val emailNoVerificado = stringResource(R.string.email_no_verificado)
    val errorDialogTitle = stringResource(R.string.error)
    val okButtonText = stringResource(R.string.ok)
    val errorDesconocido = stringResource(R.string.error_desconocido)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(loginTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.icono_atras)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = loginTitle, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(usuarioLabel) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(contraseñaLabel) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user != null && !user.isEmailVerified) {
                                    alertMessage = emailNoVerificado
                                    showAlert = true
                                    Log.e("LogIn", "Email not verified")
                                } else {
                                    Log.i("LogIn", "✅ Firebase login successful")
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            } else {
                                Log.e("LogIn", "❌ Firebase login failed", task.exception)
                                alertMessage = task.exception?.localizedMessage ?: errorDesconocido
                                showAlert = true
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(loginTitle)
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { navController.navigate("register") }
            ) {
                Text(noTienesCuenta)
            }

            Spacer(modifier = Modifier.height(5.dp))

            TextButton(
                onClick = { navController.navigate("forgot_password") }
            ) {
                Text(olvidasteContraseña)
            }
        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(errorDialogTitle) },
                text = { Text(alertMessage) },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text(okButtonText)
                    }
                }
            )
        }
    }
}
