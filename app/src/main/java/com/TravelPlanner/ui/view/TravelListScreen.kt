package com.TravelPlanner.ui.view

import android.util.Log
import android.view.ContextThemeWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelListScreen(
    navController: NavHostController,
    viewModel: TravelListViewModel = hiltViewModel()
) {
    val travelItems by viewModel.travelItems.collectAsState()
    val editingItemId by viewModel.editingItemId.collectAsState()
    val editingActivityId = remember { mutableStateOf<Int?>(null) }

    val username = FirebaseAuth.getInstance().currentUser?.email ?: "Invitado"
    val filteredTravelItems = travelItems.filter { it.userName == username }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.Lista_de_Viajes)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopEditing()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
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
                        fechafinal = (LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000),
                        userName = username
                    )
                    viewModel.addTravelItem(newItem)
                    viewModel.startEditing(newItem.id)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.content_description_add_travel)
                )
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTravelItems) { item ->
                    val isEditing = editingItemId == item.id
                    val editingActivity = item.activities.find { it.activity_id == editingActivityId.value }
                    TravelListItem(
                        item = item,
                        isEditing = isEditing,
                        editingActivity = editingActivity,
                        onEditClick = { viewModel.startEditing(item.id) },
                        onDeleteClick = { viewModel.deleteTravelItem(item) },
                        onSaveClick = {
                            viewModel.updateTravelItem(it)
                            viewModel.stopEditing()
                            editingActivityId.value = null
                        },
                        viewModel = viewModel,
                        navController = navController,
                        onAddActivity = {
                            // Bugfix: No asignar manualmente activity_id, dejar que Room lo autogenere
                            val newActivity = ActivityItems(
                                activity_id = 0, // Room lo autogenerará
                                nameActivity = "",
                                ubicacion = "",
                                duration = 0
                            )
                            viewModel.addActivityToTravel(item.id, newActivity)
                            // No modificar editingActivityId aquí, se actualizará cuando Room devuelva la nueva lista
                        },
                        onEditActivity = { activityId ->
                            editingActivityId.value = activityId
                        },
                        onCloseActivityEdit = {
                            editingActivityId.value = null
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
    navController: NavHostController,
    isEditing: Boolean,
    editingActivity: ActivityItems?,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: (TravelItem) -> Unit,
    viewModel: TravelListViewModel,
    onAddActivity: () -> Unit,
    onEditActivity: (Int) -> Unit,
    onCloseActivityEdit: () -> Unit
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
            onDismissRequest = { viewModel.stopEditing(); onCloseActivityEdit() },
            title = { Text(stringResource(R.string.EditarViajeAct)) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- Datos del viaje ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(stringResource(R.string.DetallesViaje), style = MaterialTheme.typography.titleMedium)
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
                            }
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
                            }
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
                            }
                            OutlinedTextField(
                                value = rating,
                                onValueChange = {
                                    rating = it
                                    ratingError = it.isBlank() || !it.isDigitsOnly() || it.toFloat() > 10 || it.toFloat() < 0
                                    if (!ratingError) {
                                        viewModel.updateTravel(item.copy(rating = it.toFloat()))
                                    }
                                },
                                label = { Text(stringResource(R.string.Valoración)) },
                                isError = ratingError
                            )
                            if (ratingError) {
                                Text(stringResource(R.string.ErrorValoración), color = Color.Red)
                            }
                            DatePickerButton(
                                label = stringResource(R.string.FechaInicio),
                                timestamp = item.fechainicio,
                                onDateSelected = { newTimestamp ->
                                    viewModel.updateTravel(item.copy(fechainicio = newTimestamp))
                                }
                            )
                            if (fechainicioError) {
                                Text(stringResource(R.string.ErrorFechaInicio), color = Color.Red)
                            }
                            DatePickerButton(
                                label = stringResource(R.string.FechaFinal),
                                timestamp = item.fechafinal,
                                onDateSelected = { newTimestamp ->
                                    viewModel.updateTravel(item.copy(fechafinal = newTimestamp))
                                }
                            )
                            if (fechafinalError) {
                                Text(stringResource(R.string.ErrorFechaFinal), color = Color.Red)
                            }
                        }
                    }

                    // --- Actividades ---
                    Text(stringResource(R.string.Actividades), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (editingActivity != null) {
                        ActivityEditDialog(
                            activity = editingActivity,
                            onActivityChange = { updated ->
                                viewModel.updateActivityInTravel(item.id, updated)
                            },
                            onDeleteClick = {
                                viewModel.removeActivityFromTravel(item.id, editingActivity)
                                onCloseActivityEdit()
                            },
                            onClose = { onCloseActivityEdit() }
                        )
                    } else {
                        // Mostrar todas las actividades, solo editables al clickar
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 350.dp)
                        ) {
                            items(item.activities) { activity ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable { onEditActivity(activity.activity_id) },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = activity.nameActivity.ifBlank { "Nueva Actividad" },
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = stringResource(R.string.Ubicacion) + ": " + activity.ubicacion,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = stringResource(R.string.Duración) + ": " + activity.duration.toString(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
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
                            viewModel.stopEditing()
                            onCloseActivityEdit()
                        },
                        modifier = Modifier.weight(1f)
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
                            fechainicioError = item.fechainicio < LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

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
                                    activities = item.activities
                                )
                                onSaveClick(updatedItem)
                                viewModel.stopEditing()
                                onCloseActivityEdit()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.Guardar),
                            maxLines = 1,
                            softWrap = false,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                    Button(
                        onClick = onAddActivity,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.AgregarActividad))
                    }
                }
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.titulo_label, item.title), style = MaterialTheme.typography.titleMedium)
                    Text(text = stringResource(R.string.ubicacion_label, item.location))
                    Text(text = stringResource(R.string.descripcion_label, item.description))
                    Text(text = stringResource(R.string.valoracion_label, item.rating))
                    Text(
                        text = stringResource(
                            R.string.fechainicio_label,
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.fechainicio))
                        )
                    )
                    Text(
                        text = stringResource(
                            R.string.fechafinal_label,
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.fechafinal))
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(R.string.actividades_label), style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp, max = 350.dp)
            ) {
                items(item.activities) { activity ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFA8B3))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = activity.nameActivity.ifBlank { "Nueva Actividad" },
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(R.string.Ubicacion) + ": " + activity.ubicacion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(R.string.Duración) + ": " + activity.duration.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(onClick = { onEditClick() }) {
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
fun ActivityEditDialog(
    activity: ActivityItems,
    onActivityChange: (ActivityItems) -> Unit,
    onDeleteClick: () -> Unit,
    onClose: () -> Unit
) {
    var activityName by remember { mutableStateOf(activity.nameActivity) }
    var location by remember { mutableStateOf(activity.ubicacion) }
    var duration by remember { mutableStateOf(activity.duration.toString()) }
    var nameError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Editar Actividad", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = activityName,
                onValueChange = {
                    activityName = it
                    nameError = it.isBlank()
                    onActivityChange(activity.copy(nameActivity = it))
                },
                label = { Text("Nombre de actividad") },
                isError = nameError,
                modifier = Modifier.fillMaxWidth()
            )
            if (nameError) {
                Text(stringResource(R.string.ErrorNombreActividad), color = Color.Red)
            }
            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                    locationError = it.isBlank()
                    onActivityChange(activity.copy(ubicacion = it))
                },
                label = { Text(stringResource(R.string.Ubicacion)) },
                isError = locationError,
                modifier = Modifier.fillMaxWidth()
            )
            if (locationError) {
                Text(stringResource(R.string.ErrorUbicacion), color = Color.Red)
            }
            OutlinedTextField(
                value = duration,
                onValueChange = {
                    duration = it
                    durationError = it.toIntOrNull() == null
                    onActivityChange(activity.copy(duration = it.toIntOrNull() ?: 0))
                },
                label = { Text(stringResource(R.string.Duración)) },
                isError = durationError,
                modifier = Modifier.fillMaxWidth()
            )
            if (durationError) {
                Text(stringResource(R.string.DuraciónError), color = Color.Red)
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Button(onClick = { onClose() }) {
                    Text("Cerrar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onDeleteClick(); onClose() }) {
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
