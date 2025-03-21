package com.example.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
                        viewModel = viewModel // Pasamos el ViewModel para manejar las actividades
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
    viewModel: TravelListViewModel
) {
    var isEditing by remember { mutableStateOf(item.isEditing) }
    var title by remember { mutableStateOf(item.title) }
    var location by remember { mutableStateOf(item.location) }
    var description by remember { mutableStateOf(item.description) }
    var rating by remember { mutableStateOf(item.rating.toString()) }
    var duration by remember { mutableStateOf(item.duration) }
    var activities by remember { mutableStateOf(item.activities) }
    var titleError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var ratingError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }

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

                    Text("Actividades", style = MaterialTheme.typography.titleMedium)
                    // Hacer las actividades deslizables horizontalmente
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        items(activities) { activity ->
                            ActivityListItem(
                                activity = activity,
                                onActivityNameChange = { newName ->
                                    activities = activities.toMutableList().apply {
                                        this[activities.indexOf(activity)] = activity.copy(nameActivity = newName)
                                    }
                                    viewModel.updateActivityInTravel(item.id, activity.copy(nameActivity = newName))
                                },
                                onActivityLocationChange = { newLocation ->
                                    activities = activities.toMutableList().apply {
                                        this[activities.indexOf(activity)] = activity.copy(ubicacion = newLocation)
                                    }
                                    viewModel.updateActivityInTravel(item.id, activity.copy(ubicacion = newLocation))
                                },
                                onActivityDurationChange = { newDuration ->
                                    activities = activities.toMutableList().apply {
                                        this[activities.indexOf(activity)] = activity.copy(duration = newDuration)
                                    }
                                    viewModel.updateActivityInTravel(item.id, activity.copy(duration = newDuration))
                                },
                                onDeleteClick = {
                                    activities = activities.toMutableList().apply { remove(activity) }
                                    viewModel.removeActivityFromTravel(item.id, activity)
                                }
                            )
                        }
                    }

                    Button(onClick = {
                        val newActivity = Activitys(
                            nameActivity = "Nueva Actividad",
                            ubicacion = "Ubicación",
                            duration = 2
                        )
                        activities = activities.toMutableList().apply { add(newActivity) }
                        viewModel.addActivityToTravel(item.id, newActivity)

                    },
                    ) {
                        Text("Agregar actividad")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val updatedItem = item.copy(
                        title = title,
                        location = location,
                        description = description,
                        rating = rating.toFloat(),
                        duration = duration,
                        activities = activities
                    )
                    onSaveClick(updatedItem)
                    isEditing = false
                }) {
                    Text("Guardar")
                }
            }
        )
    } else {
        Column {
            // Muestra los detalles del viaje
            Text(text = "Título: $title")
            Text(text = "Ubicación: $location")
            Text(text = "Descripción: $description")
            Text(text = "Valoración: $rating")
            Text(text = "Duración: $duration días")
            Text(text = "Actividades:")
            // Deslizante de actividades en lugar de columnas
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                items(activities) { activity ->
                    Text(text = "Actividad: ${activity.nameActivity}")
                }
            }

            // Botones para editar y eliminar
            Row {
                Button(onClick = { isEditing = true }) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onDeleteClick() }) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun ActivityListItem(
    activity: Activitys,
    onActivityNameChange: (String) -> Unit,
    onActivityLocationChange: (String) -> Unit,
    onActivityDurationChange: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    var activityName by remember { mutableStateOf(activity.nameActivity) }
    var location by remember { mutableStateOf(activity.ubicacion) }
    var duration by remember { mutableStateOf(activity.duration.toString()) }
    var nameError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp).width(220.dp)) {
        OutlinedTextField(
            value = activityName,
            onValueChange = {
                activityName = it
                nameError = it.isBlank()
                onActivityNameChange(it)
            },
            label = { Text("Nombre de actividad") },
            isError = nameError,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError) Text("El nombre de la actividad es obligatorio", color = Color.Red)

        OutlinedTextField(
            value = location,
            onValueChange = {
                location = it
                locationError = it.isBlank()
                onActivityLocationChange(it)
            },
            label = { Text("Ubicación de actividad") },
            isError = locationError,
            modifier = Modifier.fillMaxWidth()
        )
        if (locationError) Text("La ubicación es obligatoria", color = Color.Red)

        OutlinedTextField(
            value = duration,
            onValueChange = {
                duration = it
                durationError = it.toIntOrNull() == null
                onActivityDurationChange(it.toIntOrNull() ?: 0)
            },
            label = { Text("Duración (días)") },
            isError = durationError,
            modifier = Modifier.fillMaxWidth()
        )
        if (durationError) Text("Ingrese una duración válida", color = Color.Red)

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar actividad")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTravelListScreen() {
    val navController = rememberNavController()
    TravelListScreen(navController)
}