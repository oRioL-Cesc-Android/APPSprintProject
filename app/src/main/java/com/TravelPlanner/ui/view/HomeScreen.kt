package com.TravelPlanner.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.TravelPlanner.R
import com.TravelPlanner.ui.theme.APPTheme
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val showMenu = remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    APPTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.MenúPrincipal), fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                    actions = {
                        IconButton(onClick = { showMenu.value = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                        }

                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false }
                        ) {
                            DropdownMenuItem(text = { Text("About") }, onClick = { navController.navigate("about"); showMenu.value = false })
                            DropdownMenuItem(text = { Text("Version") }, onClick = { navController.navigate("version"); showMenu.value = false })
                            DropdownMenuItem(text = { Text(stringResource(R.string.Ajustes)) }, onClick = { navController.navigate("SettingsScreen"); showMenu.value = false })
                            DropdownMenuItem(text = { Text(stringResource(R.string.TerminosCond)) }, onClick = { navController.navigate("TermsCondScreen"); showMenu.value = false })
                            DropdownMenuItem(text = { Text(stringResource(R.string.LogOut))}, onClick = {
                                auth.signOut()
                                Log.i("LogOut","Sesión cerrada correctamente" )
                                navController.navigate("login");
                                showMenu.value = false
                            })
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        text = stringResource(R.string.Bienvenido),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )

                    Button(
                        onClick = { navController.navigate("Travel List") },
                        colors = buttonColors,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) { Text(stringResource(R.string.ListaViajes)) }

                    Button(
                        onClick = { navController.navigate("Trip Details Screen") },
                        colors = buttonColors,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) { Text(stringResource(R.string.DetallesViaje)) }

                    Button(
                        onClick = { navController.navigate("Map Screen") },
                        colors = buttonColors,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) { Text(stringResource(R.string.Mapa)) }

                    Button(
                        onClick = { navController.navigate("ExploreScreen") },
                        colors = buttonColors,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) { Text(stringResource(R.string.ExploreScreen)) }
                }
            }
        }
    }
}
