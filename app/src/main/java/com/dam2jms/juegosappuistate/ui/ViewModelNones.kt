package com.dam2jms.juegosappuistate.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dam2jms.juegosappuistate.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelNones : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onChange(seleccionJugadorUi: String, numeroJugadorUi: Int){
        _uiState.update {
            currentState -> currentState.copy(seleccionJugador = seleccionJugadorUi, numeroJugador = numeroJugadorUi)
        }
    }

    fun onJuego(context: Context){
        // genero de forma aleatoria que el PC escoja entre pares o nones
        val seleccionPC: String = if (_uiState.value.seleccionJugador == "nones") "pares" else "nones"

        // genero un numero aleatorio entre 1 y 5 para la PC
        val numeroPC = (1..5).random()
        val suma = _uiState.value.numeroJugador!! + numeroPC!!

        //compruebo que la seleccion del jugador solo pueda ser pares o nones
        if (_uiState.value.seleccionJugador != "pares" || _uiState.value.seleccionJugador != "nones") {

            // compruebo que el numero del jugador sea entre 1 y 5
            if (_uiState.value.numeroJugador in 1..5) {

                // si es nones y es impar la suma o si es pares y es par la suma
                if ((_uiState.value.seleccionJugador == "nones" && suma % 2 != 0) || (_uiState.value.seleccionJugador == "pares" && suma % 2 == 0)) {
                    _uiState.value.resultado = "ganas"
                    _uiState.update { currentState ->
                        currentState.copy(puntuacionJugador = currentState.puntuacionJugador.inc())
                    }
                } else {
                    _uiState.value.resultado = "pierdes"
                    _uiState.update { currentState ->
                        currentState.copy(puntuacionJugador = currentState.puntuacionJugador.dec())
                    }
                }

                //muestro el resultado
                _uiState.value.resultado = "Jugador: ${_uiState.value.seleccionJugador} ${_uiState.value.numeroJugador}\n PC: $seleccionPC. ${numeroPC}\n Resultado: ${_uiState.value.resultado}"

            } else {
                Toast.makeText(context, "El numero debe ser entre 1 y 5", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Solo puedes elejir entre pares o nones", Toast.LENGTH_SHORT).show()
        }
    }


}