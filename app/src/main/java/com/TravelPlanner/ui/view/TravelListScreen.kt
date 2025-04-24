package com.TravelPlanner.ui.view

import android.util.Log
import android.view.ContextThemeWrapper
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.TravelPlanner.R
import com.TravelPlanner.models.ActivityItems
import com.TravelPlanner.models.TravelItem
import com.TravelPlanner.ui.viewmodel.TravelListViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

//import com.example.app.models.TravelItem




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
                        fechainicio = (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000),

                        fechafinal = (LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000)
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(), // Permite desplazamiento vertical
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre elementos
            ) {
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
    var fechainicio by remember { mutableStateOf(item.fechainicio.toString()) }
    var fechafinal by remember { mutableStateOf(item.fechafinal.toString()) }






    var titleError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var ratingError by remember { mutableStateOf(false) }
    var fechainicioError by remember { mutableStateOf(false) }
    var fechafinalError by remember { mutableStateOf(false) }

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
                            if (!titleError) {
                                viewModel.updateTravel(item.copy(title = it))
                            }
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
                            if (!locationError) {
                                viewModel.updateTravel(item.copy(location = it))
                            }
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
                            if (!descriptionError) {
                                viewModel.updateTravel(item.copy(description = it))
                            }
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
                            ratingError = it.isBlank() || !it.isDigitsOnly() || it.toFloat() > 10 || it.toFloat() < 0
                            if(!ratingError) {
                                viewModel.updateTravel(item.copy(rating = it.toFloat()))
                            }
                        },
                        label = { Text(stringResource(R.string.Valoración)) },
                        isError = ratingError
                    )
                    if (ratingError){
                        Text(stringResource(R.string.ErrorValoración), color = Color.Red)
                        Log.e("ListScreen", "Error valorción")}

                    DatePickerButton(
                        label = stringResource(R.string.FechaInicio), // Nombre del campo
                        timestamp = item.fechainicio, // Valor actual en formato Long
                        onDateSelected = { newTimestamp ->
                            viewModel.updateTravel(item.copy(fechainicio = newTimestamp)) // Guarda la nueva fecha
                        }
                    )


                    if (fechainicioError){
                        Text(stringResource(R.string.ErrorFechaInicio), color = Color.Red)
                        Log.e("ListScreen", "Fecha inicio obligatoria") }

                    DatePickerButton(
                        label = stringResource(R.string.FechaFinal), // Nombre del campo
                        timestamp = item.fechafinal, // Valor actual en formato Long
                        onDateSelected = { newTimestamp ->
                            viewModel.updateTravel(item.copy(fechafinal = newTimestamp)) // Guarda la nueva fecha
                        }
                    )
                    if (fechafinalError){
                        Text(stringResource(R.string.ErrorFechaFinal), color = Color.Red)
                        Log.e("ListScreen", "Fecha inicio obligatoria") }

                    Text(stringResource(R.string.Actividades), style = MaterialTheme.typography.titleMedium)
                    // Hacer las actividades deslizables horizontalmente
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {

                        items(item.activities) { activity ->
                            ActivityListItem(
                                activity = activity,
                                onActivityNameChange = { newName ->
                                    viewModel.updateActivityInTravel(item.id, activity.copy(nameActivity = newName))
                                },
                                onActivityLocationChange = { newLocation ->
                                    viewModel.updateActivityInTravel(item.id, activity.copy(ubicacion = newLocation))
                                },
                                onActivityDurationChange = { newDuration ->
                                    viewModel.updateActivityInTravel(item.id, activity.copy(duration = newDuration))
                                },
                                onDeleteClick = {
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
                            val newActivity = ActivityItems(
                                activity_id = 0,
                                nameActivity = "",
                                ubicacion = "",
                                duration = 0
                            )
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
                        enabled = !ratingError && !titleError && !locationError && !descriptionError && !fechainicioError && !fechafinalError,
                        onClick = {
                            titleError = item.title.isBlank()
                            locationError = item.location.isBlank()
                            descriptionError = item.description.isBlank()
                            ratingError = item.rating < 0 || item.rating > 10
                            fechafinalError = item.fechafinal < item.fechainicio
                            fechainicioError = item.fechainicio < LocalDateTime.now().toEpochSecond(
                                ZoneOffset.UTC)

                            if (titleError || locationError || descriptionError || ratingError || fechainicioError || fechafinalError) {
                                errorMessage = "Por favor, completa todos los campos correctamente antes de guardar."
                                showErrorDialog = true
                            } else {
                                val updatedItem = item.copy(
                                    title = item.title,
                                    location = item.location,
                                    description = item.description,
                                    rating = item.rating,
                                    fechainicio = item.fechainicio,
                                    fechafinal = item.fechafinal,
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
            Text(text = stringResource(R.string.titulo_label, item.title))
            Text(text = stringResource(R.string.ubicacion_label, item.location))
            Text(text = stringResource(R.string.descripcion_label, item.description))
            Text(text = stringResource(R.string.valoracion_label, item.rating))
            Text(text = stringResource(R.string.fechainicio_label, SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.fechainicio))))
            Text(text = stringResource(R.string.fechafinal_label, SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.fechafinal))))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    label: String,
    timestamp: Long?,
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // ✅ Si no hay fecha seleccionada, usa la fecha actual correctamente
    val defaultTimestamp = timestamp ?: System.currentTimeMillis()
    calendar.timeInMillis = defaultTimestamp

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf(
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(defaultTimestamp))
    ) }

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val dialog = android.app.DatePickerDialog(
            ContextThemeWrapper(context, R.style.MyDatePickerDialog),
            { _, selectedYear, selectedMonth, selectedDay ->
                val newCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val newTimestamp = newCalendar.timeInMillis
                selectedDate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(newTimestamp))
                onDateSelected(newTimestamp)
            },
            year, month, day
        )




        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        showDatePicker = false
    }

    Button(onClick = { showDatePicker = true }) {
        Text(text = "$label: $selectedDate")
    }
}







@Composable
fun ActivityListItem(
    activity: ActivityItems,
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

    Card(
        modifier = Modifier
            .fillMaxWidth() // Se expande en el ancho
            .wrapContentHeight() // Se expande en la altura
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ){
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
}

@Preview(showBackground = true)
@Composable
fun PreviewTravelListScreen() {
    val navController = rememberNavController()
    TravelListScreen(navController)
}