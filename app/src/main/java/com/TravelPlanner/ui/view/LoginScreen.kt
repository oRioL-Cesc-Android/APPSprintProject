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
    val username_default = stringResource(R.string.default_user)
    val pass_default = stringResource(R.string.default_pass)
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.Login)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Icon"
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
            Text(text = stringResource(R.string.Login), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.usuario)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.contraseña)) },
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
                                // Verificar si el usuario ha confirmado su correo electrónico
                                if (user != null && !user.isEmailVerified) {
                                    // Mostrar mensaje de error si el usuario no ha verificado su correo
                                    alertMessage = "Email no verificado"
                                    showAlert = true
                                    Log.e("LogIn", "Email not verified")
                                } else {
                                    // Si el correo está verificado, navega a la pantalla de inicio
                                    Log.i("LogIn", "✅ Firebase login successful")
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            } else {
                                Log.e("LogIn", "❌ Firebase login failed", task.exception)
                                alertMessage = task.exception?.localizedMessage ?: "Error desconocido"
                                showAlert = true
                            }
                        }


                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.Login))
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = {
                    navController.navigate("register")
                },
            ) {
                Text(text = stringResource(R.string.NoTienesCuenta_Regístrate))
            }

        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Error") },
                text = { Text(alertMessage) },

                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
