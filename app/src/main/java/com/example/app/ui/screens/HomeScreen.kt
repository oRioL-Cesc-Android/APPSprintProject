package com.example.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    // Estado para controlar la visibilidad del menú desplegable
    val showMenu = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    // Icono de menú desplegable
                    IconButton(onClick = { showMenu.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }

                    // Menú desplegable
                    DropdownMenu(
                        expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false }
                    ) {
                        // Opción "About"
                        DropdownMenuItem(
                            text = { Text("About") },
                            onClick = {
                                navController.navigate("about")
                                showMenu.value = false // Cierra el menú después de la navegación
                            }
                        )

                        // Opción "Version"
                        DropdownMenuItem(
                            text = { Text("Version") },
                            onClick = {
                                navController.navigate("version")
                                showMenu.value = false // Cierra el menú después de la navegación
                            }
                        )

                        // Opción "Login"
                        DropdownMenuItem(
                            text = { Text("Login") },
                            onClick = {
                                navController.navigate("login")
                                showMenu.value = false // Cierra el menú después de la navegación
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically) // Corrected this line
            ) {
                // Botón para ir a la pantalla "Travel List"
                Button(onClick = { navController.navigate("Travel List") }) {
                    Text("Travel List")
                }

                // Botón para ir a la pantalla "Trip Details Screen"
                Button(onClick = { navController.navigate("Trip Details Screen") }) {
                    Text("Trip Details")
                }

                // Botón para ir a la pantalla "Map Screen"
                Button(onClick = { navController.navigate("Map Screen") }) {
                    Text("Map")
                }

                // Botón para ir a la pantalla "ExploreScreen"
                Button(onClick = { navController.navigate("ExploreScreen") }) {
                    Text("Explore")
                }
            }
        }
    }
}
