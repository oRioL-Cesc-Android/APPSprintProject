package com.example.app.ui.screens

import android.util.Log
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
    val editingItemId by viewModel.editingItemId.collectAsState()

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
                        title = "",
                        location = "",
                        description = "",
                        rating = 0f,
                        duration = ""
                    )
                    viewModel.addTravelItem(newItem)
                    viewModel.startEditing(newItem.id) // <- ahora controlamos edición desde ViewModel
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Agregar viaje")
                Log.i("ListItem", "Travel added successfully")
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
                    val isEditing = editingItemId == item.id
                    TravelListItem(
                        item = item,
                        isEditing = isEditing,
                        onEditClick = { viewModel.startEditing(item.id) },
                        onDeleteClick = { viewModel.deleteTravelItem(item) },
                        onSaveClick = {
                            viewModel.saveUpdatedTravelItem(it)
                            viewModel.stopEditing()
                        },
                        viewModel = viewModel,
                        navController = navController
                    )
                }

            }
        }
    }
}

@Composable
fun TravelListItem(
    item: TravelItem,
    navController: NavHostController,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: (TravelItem) -> Unit,
    viewModel: TravelListViewModel
) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val editingItemId by viewModel.editingItemId.collectAsState()
    val isEditing = item.id == editingItemId

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
            onDismissRequest = { viewModel.stopEditing() },
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
                    if (titleError) {
                        Text("El título es obligatorio", color = Color.Red)
                        Log.e("ListScreen", "Error titulo")}


                    OutlinedTextField(
                        value = location,
                        onValueChange = {
                            location = it
                            locationError = it.isBlank()
                        },
                        label = { Text("Ubicación") },
                        isError = locationError
                    )
                    if (locationError) {
                            Text("La ubicación es obligatoria", color = Color.Red)
                            Log.e("ListScreen", "Error ubicación")}

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            descriptionError = it.isBlank()
                        },
                        label = { Text("Descripción") },
                        isError = descriptionError
                    )
                    if (descriptionError) {
                        Text("La descripción es obligatoria", color = Color.Red)
                        Log.e("ListScreen", "Error descripción")
                    }

                    OutlinedTextField(
                        value = rating,
                        onValueChange = {
                            rating = it
                            ratingError = it.toFloatOrNull() == null
                        },
                        label = { Text("Valoración") },
                        isError = ratingError
                    )
                    if (ratingError){
                        Text("Ingrese una valoración válida (número)", color = Color.Red)
                        Log.e("ListScreen", "Error valorción")}

                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            duration = it
                            durationError = it.isBlank()
                        },
                        label = { Text("Duración") },
                        isError = durationError
                    )
                    if (durationError){
                        Text("La duración es obligatoria", color = Color.Red)
                        Log.e("ListScreen", "Duración obligatoria") }

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


                }
            },

            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val newActivity = Activitys(
                                nameActivity = "",
                                ubicacion = "",
                                duration = 0
                            )
                            activities = activities.toMutableList().apply { add(newActivity) }
                            viewModel.addActivityToTravel(item.id, newActivity)
                            Log.i("ListScreen", "Actividad Agregada")
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Agregar",
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate("Travel List")
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.Cancelar),
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    Button(
                        onClick = {
                            titleError = title.isBlank()
                            locationError = location.isBlank()
                            descriptionError = description.isBlank()
                            ratingError = rating.toFloatOrNull() == null
                            durationError = duration.isBlank()

                            if (titleError || locationError || descriptionError || ratingError || durationError) {
                                errorMessage = "Por favor, completa todos los campos correctamente antes de guardar."
                                showErrorDialog = true
                            } else {
                                val updatedItem = item.copy(
                                    title = title,
                                    location = location,
                                    description = description,
                                    rating = rating.toFloat(),
                                    duration = duration,
                                    activities = activities
                                )
                                onSaveClick(updatedItem)
                                viewModel.stopEditing()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Guardar",
                            maxLines = 1,
                            softWrap = false
                        )
                    }
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
                Button(onClick = { viewModel.startEditing(item.id) }) {
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
        if (nameError){ Text("El nombre de la actividad es obligatorio", color = Color.Red)
            Log.e("ListScreen", "Error nombre actividad")}

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
        if (locationError) { Text("La ubicación es obligatoria", color = Color.Red)
            Log.e("ListScreen", "Ubicación Error")}

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
        if (durationError){ Text(stringResource(R.string.DuraciónError), color = Color.Red)
            Log.e("ListScreen", "Duración incorrecta") }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.EliminarActividad))
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