package com.example.app.ui.screens




import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsCondScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminos y Condiciones") },
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
            Text(text = "Terminos y Condiciones", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Al utilizar TravelPlanner, aceptas estos términos y condiciones, así como nuestra Política de Privacidad. Si no estás de acuerdo con alguno de estos términos, te recomendamos no utilizar la aplicación." +

                    "Debes ser mayor de edad o contar con el consentimiento de un tutor legal para utilizar la aplicación. Es responsabilidad del usuario proporcionar información veraz y mantener la confidencialidad de sus credenciales de acceso. La aplicación debe utilizarse únicamente para fines legales y de acuerdo con estos términos. Queda prohibido cualquier uso fraudulento, abusivo o que viole derechos de terceros." +

                    "Las reservas realizadas a través de la aplicación están sujetas a disponibilidad y confirmación por parte de los proveedores de servicios (hoteles, aerolíneas, etc.). Los precios mostrados son responsabilidad de los proveedores. TravelPlanner no se hace responsable por errores en los precios o tarifas. Las políticas de cancelación y reembolso dependen de cada proveedor. Te recomendamos revisar estas condiciones antes de realizar una reserva." +

                    "Es responsabilidad del usuario verificar la exactitud de la información proporcionada (fechas, destinos, datos personales, etc.). El usuario es responsable de cumplir con los requisitos de viaje, como visas, pasaportes y vacunas.")

        }
    }
}