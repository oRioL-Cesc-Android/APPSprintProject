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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.app.R
import com.example.app.ui.viewmodel.TravelListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
//import com.example.app.models.TravelItem

// Modelo de datos
data class TravelItem(
    val id: Int,
    var title: String,
    var location: String,
    var description: String,
    var rating: Float,
    var duration: String,
    var activities: List<Activitys> = emptyList(),
    var isEditing: Boolean = false
)
data class Activitys (
    val nameActivity: String,
    val ubicacion: String,
    var duration: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelListScreen(
    navController: NavHostController,
    viewModel: TravelListViewModel = hiltViewModel()
) {
    val travelItems by viewModel.travelItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Viajes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Icon")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newItem = TravelItem(
                        id = (travelItems.maxOfOrNull { it.id } ?: 0) + 1,
                        title = "Nuevo Viaje",
                        location = "Destino",
                        description = "Descripción breve",
                        rating = 5.0f,
                        duration = "3 días"
                    )
                    viewModel.addTravelItem(newItem)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black

                contentColor = MaterialTheme.colorScheme.onPrimary

            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Agregar viaje")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(travelItems) { item ->
                    TravelListItem(
                        item = item,
                        onEditClick = { viewModel.updateTravelItem(item.copy(isEditing = true)) },
                        onDeleteClick = { viewModel.deleteTravelItem(item) },
                        onSaveClick = { viewModel.saveUpdatedTravelItem(it) },
                        onAddActivityClick = { travelId ->
                            val newActivity = Activitys(
                                nameActivity = "Nueva Actividad",
                                ubicacion = "Ubicación",
                                duration = 2
                            )
                            viewModel.addActivityToTravel(travelId, newActivity)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TravelListItem(
    item: TravelItem,
    onEditClick: (TravelItem) -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: (TravelItem) -> Unit,
    onAddActivityClick: (Int) -> Unit
) {
    var isEditing by remember { mutableStateOf(item.isEditing) }
    var title by remember { mutableStateOf(item.title) }
    var location by remember { mutableStateOf(item.location) }
    var description by remember { mutableStateOf(item.description) }
    var rating by remember { mutableStateOf(item.rating.toString()) }
    var duration by remember { mutableStateOf(item.duration) }
    var activities by remember { mutableStateOf(item.activities.toMutableList()) }

    var titleError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var ratingError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }
    var activitiesError by remember { mutableStateOf(false) }

    if (isEditing) {
        AlertDialog(
            onDismissRequest = { isEditing = false },
            title = { Text("Editar Viaje y Actividades") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleError = it.isBlank()
                        },
                        label = { Text("Título") },
                        isError = titleError
                    )
                    if (titleError) Text("El título es obligatorio", color = Color.Red)

                    OutlinedTextField(
                        value = location,
                        onValueChange = {
                            location = it
                            locationError = it.isBlank()
                        },
                        label = { Text("Ubicación") },
                        isError = locationError
                    )
                    if (locationError) Text("La ubicación es obligatoria", color = Color.Red)

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            descriptionError = it.isBlank()
                        },
                        label = { Text("Descripción") },
                        isError = descriptionError
                    )
                    if (descriptionError) Text("La descripción es obligatoria", color = Color.Red)

                    OutlinedTextField(
                        value = rating,
                        onValueChange = {
                            rating = it
                            ratingError = it.toFloatOrNull() == null
                        },
                        label = { Text("Valoración") },
                        isError = ratingError
                    )
                    if (ratingError) Text("Ingrese una valoración válida (número)", color = Color.Red)

                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            duration = it
                            durationError = it.isBlank()
                        },
                        label = { Text("Duración") },
                        isError = durationError
                    )
                    if (durationError) Text("La duración es obligatoria", color = Color.Red)

                    Text("Actividades:", style = MaterialTheme.typography.titleSmall)
                    activities.forEachIndexed { index, activity ->
                        Column {
                            OutlinedTextField(
                                value = activity.nameActivity,
                                onValueChange = {
                                    activities[index] = activity.copy(nameActivity = it)
                                    activitiesError = it.isBlank()
                                },
                                label = { Text("Nombre de la actividad") },
                                isError = activitiesError
                            )
                            if (activitiesError) Text("El nombre de la actividad es obligatorio", color = Color.Red)

                            OutlinedTextField(
                                value = activity.ubicacion,
                                onValueChange = {
                                    activities[index] = activity.copy(ubicacion = it)
                                    activitiesError = it.isBlank()
                                },
                                label = { Text("Ubicación") },
                                isError = activitiesError
                            )
                            if (activitiesError) Text("La ubicación de la actividad es obligatoria", color = Color.Red)

                            OutlinedTextField(
                                value = activity.duration.toString(),
                                onValueChange = {
                                    activities[index] = activity.copy(duration = it.toIntOrNull() ?: activity.duration)
                                    activitiesError = it.toIntOrNull() == null
                                },
                                label = { Text("Duración en horas") },
                                isError = activitiesError
                            )
                            if (activitiesError) Text("Ingrese una duración válida (número de horas)", color = Color.Red)

                            Button(onClick = { activities.removeAt(index) }) {
                                Text("Eliminar Actividad")
                            }
                        }
                    }
                    Button(onClick = {
                        activities.add(Activitys("Nueva Actividad", "Ubicación", 2))
                    }) {
                        Text("Agregar Nueva Actividad")
                    }
                }
            },
            confirmButton = {
                val hasErrors = titleError || locationError || descriptionError || ratingError || durationError || activitiesError
                Button(
                    onClick = {
                        if (!hasErrors) {
                            val updatedItem = item.copy(
                                title = title,
                                location = location,
                                description = description,
                                rating = rating.toFloatOrNull() ?: item.rating,
                                duration = duration,
                                activities = activities,
                                isEditing = false
                            )
                            onSaveClick(updatedItem)
                            isEditing = false
                        }
                    },
                    enabled = !hasErrors
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { isEditing = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTravelListScreen() {
    val navController = rememberNavController()
    TravelListScreen(navController)
}
