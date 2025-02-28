package com.example.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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

            // Botón para ir a la pantalla "About"
            Button(onClick = { navController.navigate("about") }) {
                Text("About")
            }

            // Botón para ir a la pantalla "Version"
            Button(onClick = { navController.navigate("version") }) {
                Text("Version")
            }

            Button(onClick = { navController.navigate("login") }) {
                Text("Login")
            }
        }
    }
}
