package com.example.app.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.app.R
import com.example.app.ui.viewmodel.TravelListViewModel

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
    val activity_id: Int = 0,  // Added this field
    val nameActivity: String,
    val ubicacion: String,
    val duration: Int  // Changed from String to Int to match your database
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
                title = { Text(stringResource(R.string.Lista_de_Viajes)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopEditing()
                        navController.navigate("home"){
                            popUpTo("home"){inclusive = true}
                        }
                    }) {
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
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.content_description_add_travel))
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
                            viewModel.updateTravelItem(it)
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
    var titleError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var ratingError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }

    if (isEditing) {
        AlertDialog(
            onDismissRequest = { viewModel.stopEditing() },
            title = { Text(stringResource(R.string.EditarViajeAct)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleError = it.isBlank()
                        },
                        label = { Text(stringResource(R.string.Titulo)) },

                        isError = titleError
                    )
                    if (titleError) {
                        Text(stringResource(R.string.ErrorTitulo), color = Color.Red)
                        Log.e("ListScreen", "Error titulo")}


                    OutlinedTextField(
                        value = location,
                        onValueChange = {
                            location = it
                            locationError = it.isBlank()
                        },
                        label = { Text(stringResource(R.string.Ubicacion)) },
                        isError = locationError
                    )
                    if (locationError) {
                            Text(stringResource(R.string.ErrorUbicacion), color = Color.Red)
                            Log.e("ListScreen", "Error ubicación")}

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            descriptionError = it.isBlank()
                        },
                        label = { Text(stringResource(R.string.Descripción)) },
                        isError = descriptionError
                    )
                    if (descriptionError) {
                        Text(stringResource(R.string.DescripciónError), color = Color.Red)
                        Log.e("ListScreen", "Error descripción")
                    }

                    OutlinedTextField(
                        value = rating,
                        onValueChange = {
                            rating = it
                            ratingError = it.toFloatOrNull() == null
                        },
                        label = { Text(stringResource(R.string.Valoración)) },
                        isError = ratingError
                    )
                    if (ratingError){
                        Text(stringResource(R.string.ErrorValoración), color = Color.Red)
                        Log.e("ListScreen", "Error valorción")}

                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            duration = it
                            durationError = it.isBlank()
                        },
                        label = { Text(stringResource(R.string.Duración)) },
                        isError = durationError
                    )
                    if (durationError){
                        Text(stringResource(R.string.ErrorDuracion), color = Color.Red)
                        Log.e("ListScreen", "Duración obligatoria") }

                    Text(stringResource(R.string.Actividades), style = MaterialTheme.typography.titleMedium)
                    // Hacer las actividades deslizables horizontalmente
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        items(item.activities) { activity ->
                            ActivityListItem(
                                activity = activity,
                                onActivityNameChange = { newName ->
//                                    activities = activities.toMutableList().apply {
//                                        this[activities.indexOf(activity)] = activity.copy(nameActivity = newName)
//                                    }
                                    viewModel.updateActivityInTravel(item.id, activity.copy(nameActivity = newName))
                                },
                                onActivityLocationChange = { newLocation ->
//                                    activities = activities.toMutableList().apply {
//                                        this[activities.indexOf(activity)] = activity.copy(ubicacion = newLocation)
//                                    }
                                    viewModel.updateActivityInTravel(item.id, activity.copy(ubicacion = newLocation))
                                },
                                onActivityDurationChange = { newDuration ->
//                                    activities = activities.toMutableList().apply {
//                                        this[activities.indexOf(activity)] = activity.copy(duration = newDuration)
//                                    }
                                    viewModel.updateActivityInTravel(item.id, activity.copy(duration = newDuration))
                                },
                                onDeleteClick = {
//                                    activities = activities.toMutableList().apply { remove(activity) }
                                    viewModel.removeActivityFromTravel(travelId = item.id, activity = activity) // ✅ Pasa ambos parámetros
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
                                activity_id = 0,
                                nameActivity = "",
                                ubicacion = "",
                                duration = 0
                            )
//                            activities = activities.toMutableList().apply { add(newActivity) }
                            viewModel.addActivityToTravel(item.id, newActivity)
                            Log.i("ListScreen", "Actividad Agregada")
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.Agregar),
                            maxLines = 1,
                            softWrap = false,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp

                        )
                    }

                    Button(
                        onClick = {
                            viewModel.stopEditing()
                            //navController.navigate("Travel List")
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.Cancelar),
                            maxLines = 1,
                            softWrap = false,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
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
                                    activities = item.activities // <-- Asegurar que se guarda la nueva lista
                                )

                                onSaveClick(updatedItem)
                                viewModel.stopEditing()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.Guardar),
                            maxLines = 1,
                            softWrap = false,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                }
            }




        )
    } else {
        Column {
            // Muestra los detalles del viaje
            Text(text = stringResource(R.string.titulo_label, title))
            Text(text = stringResource(R.string.ubicacion_label, location))
            Text(text = stringResource(R.string.descripcion_label, description))
            Text(text = stringResource(R.string.valoracion_label, rating))
            Text(text = stringResource(R.string.duracion_label, duration))
            Text(text = stringResource(R.string.actividades_label))

            // Deslizante de actividades en lugar de columnas
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                items(item.activities) { activity ->
                    Text(text = stringResource(R.string.actividad_label, activity.nameActivity))

                }
            }

            // Botones para editar y eliminar
            Row {
                Button(onClick = { viewModel.startEditing(item.id) }) {
                    Text(stringResource(R.string.Editar))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onDeleteClick() }) {
                    Text(stringResource(R.string.Eliminar))
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
        if (nameError){ Text(stringResource(R.string.ErrorNombreActividad), color = Color.Red)
            Log.e("ListScreen", "Error nombre actividad")}

        OutlinedTextField(
            value = location,
            onValueChange = {
                location = it
                locationError = it.isBlank()
                onActivityLocationChange(it)
            },
            label = { Text(stringResource(R.string.Ubicacion)) },
            isError = locationError,
            modifier = Modifier.fillMaxWidth()
        )
        if (locationError) { Text(stringResource(R.string.ErrorUbicacion), color = Color.Red)
            Log.e("ListScreen", "Ubicación Error")}

        OutlinedTextField(
            value = duration,
            onValueChange = {
                duration = it
                durationError = it.toIntOrNull() == null
                onActivityDurationChange(it.toIntOrNull() ?: 0)
            },
            label = { Text(stringResource(R.string.Duración)) },
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