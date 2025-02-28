package com.example.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class TravelItem(
    val id: Int,
    var title: String,          // Título del viaje
    var location: String,       // Localización
    var description: String,    // Descripción
    var rating: Float,          // Valoración (puede ser un número decimal)
    var duration: String,       // Duración (puede ser un String como "3 días")
    var isEditing: Boolean = false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelListScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Viajes") }, // Título de la barra superior
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Usa el padding proporcionado por Scaffold
                .padding(16.dp) // Margen adicional para el contenido
        ) {
            ListApp(navController) // Llama al componente que definiste anteriormente.
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListApp(navController: NavHostController) {
    // Datos de ejemplo para la aplicación
    var sItems by remember { mutableStateOf(listOf<TravelItem>()) }
    var itemTitle by remember { mutableStateOf("") }
    var itemLocation by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var itemRating by remember { mutableStateOf("") }
    var itemDuration by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showDialog = true } // Abre el diálogo al hacer clic
            ) {
                Text("Agregar Viaje")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sItems) { item ->
                    if (item.isEditing) {
                        TravelItemEditor(
                            item = item,
                            onEditComplete = { title, location, description, rating, duration ->
                                sItems = sItems.map { it.copy(isEditing = false) }
                                val editedItem = sItems.find { it.id == item.id }
                                editedItem?.let {
                                    it.title = title
                                    it.location = location
                                    it.description = description
                                    it.rating = rating
                                    it.duration = duration
                                }
                            }
                        )
                    } else {
                        TravelListItem(
                            item = item,
                            onEditClick = { sItems = sItems.map { it.copy(isEditing = it.id == item.id) } },
                            onDeleteClick = { sItems = sItems - item }
                        )
                    }
                }
            }
        }

        // Botón para volver al menú de inicio


        // Diálogo para agregar un nuevo viaje
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // Cierra el diálogo al tocar fuera
                title = { Text("Agregar Viaje") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemTitle,
                            onValueChange = { itemTitle = it },
                            label = { Text("Título") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = itemLocation,
                            onValueChange = { itemLocation = it },
                            label = { Text("Localización") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = itemDescription,
                            onValueChange = { itemDescription = it },
                            label = { Text("Descripción") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = itemRating,
                            onValueChange = { itemRating = it },
                            label = { Text("Valoración (0-10)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = itemDuration,
                            onValueChange = { itemDuration = it },
                            label = { Text("Duración") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (itemTitle.isNotBlank() && itemLocation.isNotBlank()) {
                                    val newItem = TravelItem(
                                        id = sItems.size + 1,
                                        title = itemTitle,
                                        location = itemLocation,
                                        description = itemDescription,
                                        rating = itemRating.toFloatOrNull() ?: 0f,
                                        duration = itemDuration
                                    )
                                    sItems = sItems + newItem
                                    showDialog = false // Cierra el diálogo
                                    itemTitle = "" // Limpia los campos
                                    itemLocation = ""
                                    itemDescription = ""
                                    itemRating = ""
                                    itemDuration = ""
                                }
                            }
                        ) {
                            Text("Agregar")
                        }
                        Button(
                            onClick = { showDialog = false } // Cierra el diálogo sin agregar
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            )
        }
    }
}
@Composable
fun TravelListItem(
    item: TravelItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Localización: ${item.location}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Descripción: ${item.description}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Valoración: ${item.rating}/10", style = MaterialTheme.typography.bodySmall)
            Text(text = "Duración: ${item.duration}", style = MaterialTheme.typography.bodySmall)
        }
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
@Composable
fun TravelItemEditor(
    item: TravelItem,
    onEditComplete: (String, String, String, Float, String) -> Unit
) {
    var editedTitle by remember { mutableStateOf(item.title) }
    var editedLocation by remember { mutableStateOf(item.location) }
    var editedDescription by remember { mutableStateOf(item.description) }
    var editedRating by remember { mutableStateOf(item.rating.toString()) }
    var editedDuration by remember { mutableStateOf(item.duration) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = editedTitle,
            onValueChange = { editedTitle = it },
            label = { Text("Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = editedLocation,
            onValueChange = { editedLocation = it },
            label = { Text("Localización") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = editedDescription,
            onValueChange = { editedDescription = it },
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = editedRating,
            onValueChange = { editedRating = it },
            label = { Text("Valoración (0-5)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = editedDuration,
            onValueChange = { editedDuration = it },
            label = { Text("Duración") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                val rating = editedRating.toFloatOrNull() ?: 0f
                onEditComplete(editedTitle, editedLocation, editedDescription, rating, editedDuration)
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text("Guardar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    val mockNavController = rememberNavController()
    ListApp(navController = mockNavController)
}