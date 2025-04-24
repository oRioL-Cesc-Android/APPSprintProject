package com.TravelPlanner.ui.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.res.stringResource
import com.TravelPlanner.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var birthdateTimestamp by remember { mutableStateOf<Long?>(null) }
    var address by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var selectedPrefix by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var acceptEmails by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val countryCodes = mapOf(
        "🇺🇸 USA" to "+1", "🇪🇸 Spain" to "+34", "🇬🇧 UK" to "+44", "🇩🇪 Germany" to "+49",
        "🇫🇷 France" to "+33", "🇲🇽 Mexico" to "+52", "🇯🇵 Japan" to "+81", "🇧🇷 Brazil" to "+55",
        "🇨🇦 Canada" to "+1", "🇦🇷 Argentina" to "+54", "🇨🇱 Chile" to "+56", "🇨🇴 Colombia" to "+57",
        "🇵🇪 Peru" to "+51", "🇮🇹 Italy" to "+39", "🇵🇹 Portugal" to "+351", "🇳🇱 Netherlands" to "+31",
        "🇧🇪 Belgium" to "+32", "🇨🇭 Switzerland" to "+41", "🇸🇪 Sweden" to "+46", "🇳🇴 Norway" to "+47",
        "🇫🇮 Finland" to "+358", "🇩🇰 Denmark" to "+45", "🇮🇳 India" to "+91", "🇨🇳 China" to "+86",
        "🇷🇺 Russia" to "+7", "🇰🇷 South Korea" to "+82", "🇦🇺 Australia" to "+61", "🇳🇿 New Zealand" to "+64",
        "🇿🇦 South Africa" to "+27", "🇪🇬 Egypt" to "+20", "🇸🇦 Saudi Arabia" to "+966",
        "🇦🇪 UAE" to "+971", "🇹🇷 Turkey" to "+90", "🇵🇰 Pakistan" to "+92", "🇮🇩 Indonesia" to "+62"
    )
    val msgEmailPasswordRequired = stringResource(R.string.error_email_password_required)
    val msgPhoneInvalid = stringResource(R.string.error_phone_invalid)
    val msgRegistrationSuccess = stringResource(R.string.registration_successful)
    val userInvalid = stringResource(R.string.userInvalid)

    val countries = countryCodes.keys.toList()
    var expandedCountry by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.register)) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.username)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            DatePickerButton(
                label = stringResource(R.string.birthdate),
                timestamp = birthdateTimestamp,
                onDateSelected = { birthdateTimestamp = it }
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.address)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedCountry,
                onExpandedChange = { expandedCountry = !expandedCountry }
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.country)) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountry) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCountry,
                    onDismissRequest = { expandedCountry = false }
                ) {
                    countries.forEach { countryItem ->
                        DropdownMenuItem(
                            text = { Text(countryItem) },
                            onClick = {
                                selectedCountry = countryItem
                                selectedPrefix = countryCodes[countryItem] ?: ""
                                expandedCountry = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedPrefix,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.prefix)) },
                    readOnly = true,
                    modifier = Modifier.width(100.dp)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() } && it.length <= 15) {
                            phone = it
                        }
                    },
                    label = { Text(stringResource(R.string.phone_number)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    isError = phone.isNotEmpty() && (phone.length < 8 || phone.length > 15)
                )
            }
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = acceptEmails, onCheckedChange = { acceptEmails = it })
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.receive_emails))
            }

            Spacer(Modifier.height(16.dp))



            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, msgEmailPasswordRequired, Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (phone.length !in 8..15) {
                        Toast.makeText(context, msgPhoneInvalid, Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (username.isBlank()) {
                        Toast.makeText(context, userInvalid, Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    loading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            loading = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, msgRegistrationSuccess, Toast.LENGTH_LONG).show()
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.registration_failed, task.exception?.localizedMessage ?: ""),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },

                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(if (loading) stringResource(R.string.registering) else stringResource(R.string.register))
            }
        }
    }
}

