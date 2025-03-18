package com.example.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.app.R
import com.example.app.ui.viewmodel.TravellistViewModel

data class TravelItem(
    val id: Int,
    var title: String,
    var location: String,
    var description: String,
    var rating: Float,
    var duration: String,
    var isEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelListScreen(
    navController: NavHostController,
    viewModel: TravellistViewModel = hiltViewModel() // Inyección de Hilt
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ListaViajes)) },
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
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            ListApp(navController) // Se pasa el navController
        }
    }
}

@Composable
fun ListApp(navController: NavHostController) {
    var sItems by remember { mutableStateOf(listOf<TravelItem>()) }
    var itemTitle by remember { mutableStateOf("") }
    var itemLocation by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var itemRating by remember { mutableStateOf("") }
    var itemDuration by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para errores en el diálogo de "Agregar viaje"
    var titleError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var ratingError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }
    var showGeneralError by remember { mutableStateOf(false) } // Para mostrar el diálogo de error general

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showDialog = true }) {
                Text(stringResource(R.string.AgregarViaje))
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
                                sItems = sItems.map { if (it.id == item.id) it.copy(
                                    title = title,
                                    location = location,
                                    description = description,
                                    rating = rating,
                                    duration = duration,
                                    isEditing = false
                                ) else it }
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

        // Diálogo para agregar un nuevo viaje
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(R.string.AgregarViaje)) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemTitle,
                            onValueChange = {
                                itemTitle = it
                                titleError = it.isBlank() // Validar si el título está vacío
                            },
                            label = { Text(stringResource(R.string.Titulo)) },
                            isError = titleError,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        if (titleError) {
                            Text(
                                text = "El título no puede estar vacío",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = itemLocation,
                            onValueChange = {
                                itemLocation = it
                                locationError = it.isBlank() // Validar si la localización está vacía
                            },
                            label = { Text(stringResource(R.string.Localización)) },
                            isError = locationError,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        if (locationError) {
                            Text(
                                text = "La localización no puede estar vacía",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = itemDescription,
                            onValueChange = {
                                itemDescription = it
                                descriptionError = it.isBlank() // Validar si la descripción está vacía
                            },
                            label = { Text(stringResource(R.string.Descripción)) },
                            isError = descriptionError,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        if (descriptionError) {
                            Text(
                                text = "La descripción no puede estar vacía",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = itemRating,
                            onValueChange = {
                                itemRating = it
                                ratingError = it.toFloatOrNull() == null || it.toFloat() < 0 || it.toFloat() > 10 // Validar el rating
                            },
                            label = { Text(stringResource(R.string.Valoración)) },
                            isError = ratingError,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        if (ratingError) {
                            Text(
                                text = "La valoración debe estar entre 0 y 10",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = itemDuration,
                            onValueChange = {
                                itemDuration = it
                                durationError = it.isBlank() // Validar si la duración está vacía
                            },
                            label = { Text(stringResource(R.string.Duración)) },
                            isError = durationError,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        if (durationError) {
                            Text(
                                text = "La duración no puede estar vacía",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                // Validar los campos antes de agregar
                                titleError = itemTitle.isBlank()
                                locationError = itemLocation.isBlank()
                                descriptionError = itemDescription.isBlank()
                                ratingError = itemRating.toFloatOrNull() == null || itemRating.toFloat() < 0 || itemRating.toFloat() > 10
                                durationError = itemDuration.isBlank()

                                // Si hay errores, mostrar el diálogo de error general
                                if (titleError || locationError || ratingError || durationError || descriptionError) {
                                    showGeneralError = true
                                } else {
                                    // Si no hay errores, agregar la nota
                                    val ratingValue = itemRating.toFloatOrNull() ?: 0f
                                    val newItem = TravelItem(
                                        id = sItems.size + 1,
                                        title = itemTitle,
                                        location = itemLocation,
                                        description = itemDescription,
                                        rating = ratingValue,
                                        duration = itemDuration
                                    )
                                    sItems = sItems + newItem
                                    showDialog = false
                                    itemTitle = ""
                                    itemLocation = ""
                                    itemDescription = ""
                                    itemRating = ""
                                    itemDuration = ""
                                }
                            }
                        ) {
                            Text(stringResource(R.string.Agregar))
                        }
                        Button(onClick = { showDialog = false }) {
                            Text(stringResource(R.string.Cancelar))
                        }
                    }
                }
            )
        }

        // Mostrar un mensaje de error general si hay errores
        if (showGeneralError) {
            AlertDialog(
                onDismissRequest = { showGeneralError = false },
                title = { Text("Error") },
                text = { Text("Por favor, completa todos los campos correctamente antes de guardar.") },
                confirmButton = {
                    Button(onClick = { showGeneralError = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun TravelItemEditor(
    item: TravelItem,
    onEditComplete: (String, String, String, Float, String) -> Unit
) {
    // Estado para los valores de los campos
    var title by remember { mutableStateOf(item.title) }
    var location by remember { mutableStateOf(item.location) }
    var description by remember { mutableStateOf(item.description) }
    var rating by remember { mutableStateOf(item.rating.toString()) }
    var duration by remember { mutableStateOf(item.duration) }

    // Estados para errores
    var titleError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var ratingError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }

    // Estado para mostrar el diálogo de error general
    var showGeneralError by remember { mutableStateOf(false) }

    // Card for layout
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(BorderStroke(2.dp, Color(0XFF018786)), RoundedCornerShape(20)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Título del viaje
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = it.isBlank()
                },
                label = { Text(stringResource(R.string.Titulo)) },
                isError = titleError,
                modifier = Modifier.fillMaxWidth()
            )
            if (titleError) {
                Text(
                    text = "El título no puede estar vacío",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            // Localización
            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                    locationError = it.isBlank()
                },
                label = { Text(stringResource(R.string.Localización)) },
                isError = locationError,
                modifier = Modifier.fillMaxWidth()
            )
            if (locationError) {
                Text(
                    text = "La localización no puede estar vacía",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            // Descripción
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = it.isBlank()
                },
                label = { Text(stringResource(R.string.Descripción)) },
                isError = descriptionError,
                modifier = Modifier.fillMaxWidth()
            )
            if (descriptionError) {
                Text(
                    text = "La descripción no puede estar vacía",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            // Valoración (Rating)
            OutlinedTextField(
                value = rating,
                onValueChange = {
                    rating = it
                    ratingError = it.toFloatOrNull() == null || it.toFloat() < 0 || it.toFloat() > 10
                },
                label = { Text(stringResource(R.string.Valoración)) },
                isError = ratingError,
                modifier = Modifier.fillMaxWidth()
            )
            if (ratingError) {
                Text(
                    text = "La valoración debe estar entre 0 y 10",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            // Duración
            OutlinedTextField(
                value = duration,
                onValueChange = {
                    duration = it
                    durationError = it.isBlank()
                },
                label = { Text(stringResource(R.string.Duración)) },
                isError = durationError,
                modifier = Modifier.fillMaxWidth()
            )
            if (durationError) {
                Text(
                    text = "La duración no puede estar vacía",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            // Botones para guardar o cancelar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    // Validaciones antes de guardar
                    titleError = title.isBlank()
                    locationError = location.isBlank()
                    descriptionError = description.isBlank()
                    ratingError = rating.toFloatOrNull() == null || rating.toFloat() < 0 || rating.toFloat() > 10
                    durationError = duration.isBlank()

                    // Si hay errores, mostrar el diálogo de error general
                    if (titleError || locationError || ratingError || durationError || descriptionError) {
                        showGeneralError = true
                    } else {
                        // Si no hay errores, llamar a onEditComplete
                        val ratingValue = rating.toFloatOrNull() ?: 0f
                        onEditComplete(title, location, description, ratingValue, duration)
                    }
                }) {
                    Text(stringResource(R.string.Guardar))
                }

                Button(onClick = {
                    onEditComplete(item.title, item.location, item.description, item.rating, item.duration)
                }) {
                    Text(stringResource(R.string.Cancelar))
                }
            }
        }
    }

    // Mostrar un mensaje de error general si hay errores
    if (showGeneralError) {
        AlertDialog(
            onDismissRequest = { showGeneralError = false },
            title = { Text("Error") },
            text = { Text("Por favor, completa todos los campos correctamente antes de guardar.") },
            confirmButton = {
                Button(onClick = { showGeneralError = false }) {
                    Text("OK")
                }
            }
        )
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
            .border(BorderStroke(2.dp, Color(0XFF018786)), RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(8.dp).weight(1f)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Localización: ${item.location}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Descripción: ${item.description}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Valoración: ${item.rating}/10", style = MaterialTheme.typography.bodySmall)
            Text(text = "Duración: ${item.duration}", style = MaterialTheme.typography.bodySmall)
        }
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.Editar))
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.Eliminar))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    TravelListScreen(navController = rememberNavController())
}