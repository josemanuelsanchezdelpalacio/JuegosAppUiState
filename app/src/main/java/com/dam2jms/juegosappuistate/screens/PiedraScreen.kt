package com.dam2jms.juegosappuistate.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dam2jms.juegosappuistate.states.NonesUiState
import com.dam2jms.juegosappuistate.ui.ViewModelPiedraState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun piedraScreenState(navController: NavController, mvvm: ViewModelPiedraState) {

    val uiState: NonesUiState by mvvm.uiState.observeAsState(NonesUiState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "VECES GANADAS: $uiState.puntuacion") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Atras")
                    }
                }
            )
        }
    ) { paddingValues ->
        piedraBodyContentState(modifier = Modifier.padding(paddingValues), mvvm, uiState)
    }
}

@Composable
fun piedraBodyContentState(modifier: Modifier, mvvm: ViewModelPiedraState, nonesUiState: NonesUiState) {

    var mostrarAlertDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = nonesUiState.seleccionJugador,
            onValueChange = { mvvm.onSeleccionJugador(seleccionJugadorUi = it) },
            label = { Text(text = "Elije entre piedra, papel o tijeras") },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (mostrarAlertDialog) {
            AlertDialog(text = {
                nonesUiState.resultado?.let { Text(text = it) }
            }, onDismissRequest = { mostrarAlertDialog = false }, confirmButton = {
                TextButton(onClick = { mostrarAlertDialog = false }) { Text(text = "OK") }
            })
        }

        Button(
            onClick = {
                //llamo al metodo del viewmodel con la logica
                mvvm.onJuego(context = context)
                mostrarAlertDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            Text(text = "JUGAR")
        }
    }
}