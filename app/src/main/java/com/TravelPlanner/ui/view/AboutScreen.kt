package com.TravelPlanner.ui.view


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.TravelPlanner.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(R.string.AboutScreen), style = MaterialTheme.typography.headlineMedium)
            Text(stringResource(R.string.AboutDescription))
            Text(
                text = stringResource(R.string.Desarrolladores),
                style = TextStyle(

                    fontWeight = FontWeight.Bold // Opcional: añade negrita
                )
            )

            Divider(thickness = 1.dp)
            Text(stringResource(R.string.Versión_actual))
            Text(text = "Contacto: udl@udl.cat")
        }
    }
}