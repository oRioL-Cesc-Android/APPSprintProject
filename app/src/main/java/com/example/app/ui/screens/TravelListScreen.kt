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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.viewmodel.TravelListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Modelo de datos
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
                contentColor = Color.White
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
                        onSaveClick = { viewModel.saveUpdatedTravelItem(it) }
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
    onSaveClick: (TravelItem) -> Unit
) {
    var isEditing by remember { mutableStateOf(item.isEditing) }
    var title by remember { mutableStateOf(item.title) }
    var location by remember { mutableStateOf(item.location) }
    var description by remember { mutableStateOf(item.description) }
    var rating by remember { mutableStateOf(item.rating.toString()) }
    var duration by remember { mutableStateOf(item.duration) }

    if (isEditing) {
        AlertDialog(
            onDismissRequest = { isEditing = false },
            title = { Text("Editar Viaje") },
            text = {
                Column {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                    OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ubicación") })
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                    OutlinedTextField(value = rating, onValueChange = { rating = it }, label = { Text("Valoración") })
                    OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duración") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    val updatedItem = item.copy(
                        title = title,
                        location = location,
                        description = description,
                        rating = rating.toFloatOrNull() ?: item.rating,
                        duration = duration,
                        isEditing = false
                    )
                    onSaveClick(updatedItem)
                    isEditing = false
                }) {
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
            IconButton(onClick = { isEditing = true }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
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
