package com.dam2jms.juegosappuistate.ui

import androidx.lifecycle.ViewModel
import com.dam2jms.juegosappuistate.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelSiete : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Propiedades para la banca
    private var apuestaBanca: Int = apuestaRandom()

    fun onJuego() {
        // Tanto el jugador como el PC obtienen una carta random y se les da el valor de esa carta
        val cartaJugador = cartas()
        val valorCartaJugador = valorCarta(cartaJugador)

        val cartaPC = cartas()
        val valorCartaPC = valorCarta(cartaPC)

        // Se van sumando cada vez que piden una carta
        val nuevaPuntuacionJugador = _uiState.value.valorCartasJugador + valorCartaJugador
        val nuevaPuntuacionPC = _uiState.value.valorCartasPC + valorCartaPC

        // Actualizo los valores de las cartas de cada jugador
        _uiState.update { currentState ->
            currentState.copy(
                valorCartasJugador = nuevaPuntuacionJugador,
                valorCartasPC = nuevaPuntuacionPC
            )
        }

        // Cuando uno de los dos llega a 7.5 ganan el doble de la apuesta
        if (nuevaPuntuacionPC == 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "Ganador PC. Gana el doble de la apuesta",
                    dineroGanadoJugador = apuestaBanca * 2
                )
            }
        } else if (nuevaPuntuacionJugador == 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "Ganaste. Ganas el doble de la apuesta",
                    dineroGanadoPC = apuestaBanca * 2
                )
            }
        }

        // Si se pasan, pagan a la banca el importe de la apuesta
        if (nuevaPuntuacionPC > 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "PC se pasó de 7.5. Paga",
                    dineroGanadoPC = -apuestaBanca,
                    apuesta = 0
                )
            }
        } else if (nuevaPuntuacionJugador > 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "Te pasaste de 7.5. Pagas",
                    dineroGanadoJugador = apuestaBanca * 2,
                    apuesta = 0
                )
            }
        }

        // Si uno de los jugadores llega a 7.5 o se pasa, reiniciar el juego
        if (nuevaPuntuacionJugador >= 7.5 || nuevaPuntuacionPC >= 7.5) {
            onReset()
        }
    }


    fun onReset() {
        _uiState.update { currentState ->
            currentState.copy(
                valorCartasJugador = 0.0,
                valorCartasPC = 0.0
            )
        }
    }

    fun apuestaRandom(): Int {
        return (100..500).random()
    }

    private fun cartas(): String {
        val cartas = listOf("As", "2", "3", "4", "5", "6", "7", "Sota", "Caballo", "Rey")
        return cartas.random()
    }

    private fun valorCarta(carta: String): Double {
        return when (carta) {
            "As" -> 1.0
            "2" -> 2.0
            "3" -> 3.0
            "4" -> 4.0
            "5" -> 5.0
            "6" -> 6.0
            "7" -> 7.0
            else -> 0.5
        }
    }
}

/*//metodo con una lista con las opciones de piedra, papel, tijeras que la randomiza
fun eleccionAleatoriaPC(): String {
    val opciones = listOf("piedra", "papel", "tijeras")
    return opciones.random()
}

@Composable
fun piedraScreen(navController: NavController) {

    var eleccionJugador by rememberSaveable { mutableStateOf("") }
    var eleccionPC by rememberSaveable { mutableStateOf("") }
    var ganador by rememberSaveable { mutableStateOf("") }
    var mostrarAlertDialog by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = eleccionJugador,
            onValueChange = { eleccionJugador = it },
            label = { Text(text = "Elije entre piedra, papel o tijeras") },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (mostrarAlertDialog) {
            AlertDialog(
                text = { Text(text = "Yo he sacado $eleccionPC. Gana $ganador") },
                onDismissRequest = { mostrarAlertDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarAlertDialog = false
                    }) { Text(text = "OK") }
                })
        }

        Button(
            onClick = {

                eleccionPC = eleccionAleatoriaPC()

                //si es nones y es impar la suma o si es pares y es par la suma muestra el alertdialog con el ganador segun la suma de los numeros
                if (eleccionJugador.equals(eleccionPC)) {
                    mostrarAlertDialog = true
                    ganador = "nadie"
                } else if (
                    (eleccionJugador.equals("piedra") && eleccionPC.equals("tijeras")) ||
                    (eleccionJugador.equals("papel") && eleccionPC.equals("piedra")) ||
                    (eleccionJugador.equals("tijeras") && eleccionPC.equals("papel"))
                ) {
                    mostrarAlertDialog = true
                    ganador = "Jugador"
                } else {
                    mostrarAlertDialog = true
                    ganador = "PC"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            Text(text = "JUGAR")
        }
    }
}


/*
* @Composable
fun AlquilarScreen(navController: NavController) {
    val context = LocalContext.current
    val listadoMaterial by remember { mutableStateOf(generateMaterialList()) }
    var material by remember { mutableStateOf("") }
    var talla by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar() {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver atrás")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver atrás")
            }
        }
    ) {
        BodyAlquilarScreen(material, talla, cantidad, listadoMaterial, context)
    }
}

@Composable
fun BodyAlquilarScreen(
    material: String,
    talla: String,
    cantidad: String,
    listadoMaterial: List<MaterialItem>,
    context: Context
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ... Otros elementos de la pantalla

        OutlinedTextField(
            value = material,
            onValueChange = { material = it },
            label = { Text(text = "Elige el material a alquilar (Esquís/Botas)") }
        )

        OutlinedTextField(
            value = talla,
            onValueChange = { talla = it },
            label = { Text(text = "Introduce talla") }
        )

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text(text = "Introduce cantidad") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                // Busca el material seleccionado en la lista y verifica si hay suficiente cantidad
                val selectedMaterial = listadoMaterial.find {
                    it.mat == material && it.talla == talla
                }

                if (selectedMaterial != null && selectedMaterial.cantidad >= cantidad.toInt()) {
                    // Actualiza la cantidad disponible del material
                    selectedMaterial.cantidad -= cantidad.toInt()
                } else {
                    Toast.makeText(context, "No hay material suficiente", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
        ) {
            Text(text = "Reservar")
        }
    }
}

data class MaterialItem(val mat: String, val talla: String, var cantidad: Int)

fun generateMaterialList(): List<MaterialItem> {
    // Genera una lista de ejemplo, reemplázala con tus datos reales
    return listOf(
        MaterialItem("botas", "42", 5),
        MaterialItem("esquis", "160", 10),
        MaterialItem("botas", "38", 2),
        MaterialItem("esquis", "170", 7)
    )
}
*/










@Composable
fun sieteScreen(navController: NavController) {
    var puntuacionJugador by rememberSaveable { mutableStateOf(0.0) }
    var puntuacionPC by rememberSaveable { mutableStateOf(0.0) }
    var mostrarAlertDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Puntuacion del Jugador: $puntuacionJugador")
        Text(text = "Puntuacion del PC: $puntuacionPC")

        if (mostrarAlertDialog) {
            AlertDialog(
                text = {
                    Text(
                        text = "Puntuacion del Jugador: $puntuacionJugador\nPuntaje del PC: $puntuacionPC\nEl juego ha terminado."
                    )
                },
                onDismissRequest = { mostrarAlertDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarAlertDialog = false
                        puntuacionJugador = 0.0
                        puntuacionPC = 0.0
                    }) {
                        Text(text = "Reiniciar")
                    }
                }
            )
        }

        Button(
            onClick = {
                val cartaJugador = obtenerCartaAleatoria()
                puntuacionJugador += obtenerValorCarta(cartaJugador)

                if (puntuacionJugador >= 7.5) {
                    mostrarAlertDialog = true
                }

                // Lógica del PC
                val cartaPC = obtenerCartaAleatoria()
                puntuacionPC += obtenerValorCarta(cartaPC)

                if (puntuacionPC >= 7.5) {
                    mostrarAlertDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Tomar Carta")
        }
    }
}

//metodo para obtener una carta aleatoria con su valor
fun obtenerCartaAleatoria(): String {
    val cartas = listOf("1", "2", "3", "4", "5", "6", "7", "Sota", "Caballo", "Rey")
    return cartas.random()
}

//metodo para obtener el valor de una carta
fun obtenerValorCarta(carta: String): Double {
    return when (carta) {
        "Sota", "Caballo", "Rey" -> 0.5
        else -> carta.toDouble()
    }
}


/*juego del uno
*
* @Composable
fun unoScreen(navController: NavController) {
    var jugadorUnoMano by rememberSaveable { mutableStateOf(List(7) { obtenerCartaAleatoria() }) }
    var jugadorDosMano by rememberSaveable { mutableStateOf(List(7) { obtenerCartaAleatoria() }) }
    var cartaActual by rememberSaveable { mutableStateOf(obtenerCartaAleatoria()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Carta actual: $cartaActual")

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la mano del jugador uno
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            jugadorUnoMano.forEach { carta ->
                Card(
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .padding(4.dp),
                    backgroundColor = Color.Red,
                    onClick = { jugarCarta(carta, jugadorUnoMano) }
                ) {
                    Text(
                        text = carta,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la mano del jugador dos
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            jugadorDosMano.forEach { carta ->
                Card(
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .padding(4.dp),
                    backgroundColor = Color.Blue,
                    onClick = { jugarCarta(carta, jugadorDosMano) }
                ) {
                    Text(
                        text = carta,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

// Función para jugar una carta
fun jugarCarta(carta: String, mano: MutableState<List<String>) {
    if (carta == cartaActual || carta.startsWith("Comodin") || cartaActual.startsWith("Comodin")) {
        // Actualiza la carta actual
        cartaActual = carta

        // Elimina la carta jugada de la mano del jugador
        mano.value = mano.value.filterNot { it == carta }
    }
}

// Función para obtener una carta aleatoria del mazo de UNO
fun obtenerCartaAleatoria(): String {
    val colores = listOf("Rojo", "Verde", "Azul", "Amarillo")
    val valores = (0..9).map { it.toString() }
    val cartasEspeciales = listOf("Salta", "Reversa", "+2")
    val comodines = listOf("Comodin", "+4")

    val cartaAleatoria = when ((0..6).random()) {
        0 -> cartasEspeciales.random()
        1 -> comodines.random()
        else -> "${colores.random()} ${valores.random()}"
    }

    return cartaAleatoria
}
*
* */



/*
* class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var numero by remember { mutableStateOf(0) }
            var resultado by remember { mutableStateOf("") }
            var isFactorial by remember { mutableStateOf(true) }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ingrese un número:",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )

                BasicTextField(
                    value = TextFieldValue(numero.toString()),
                    onValueChange = { value ->
                        numero = value.text.toIntOrNull() ?: 0
                    },
                    modifier = Modifier.padding(16.dp)
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            isFactorial = true
                            resultado = calculateFactorial(numero).toString()
                        },
                        enabled = numero >= 0
                    ) {
                        Text(text = "Factorial")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            isFactorial = false
                            resultado = calculateDivisors(numero).joinToString()
                        },
                        enabled = numero >= 0
                    ) {
                        Text(text = "Divisores")
                    }
                }

                if (resultado.isNotEmpty()) {
                    val operationText = if (isFactorial) "Factorial" else "Divisores"
                    Text(
                        text = "Resultado del $operationText de $numero es:",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = resultado,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    private fun calculateFactorial(num: Int): Long {
        if (num <= 1) return 1
        var result: Long = 1
        for (i in 2..num) {
            result *= i
        }
        return result
    }

    private fun calculateDivisors(num: Int): List<Int> {
        val divisors = mutableListOf<Int>()
        for (i in 1..num) {
            if (num % i == 0) {
                divisors.add(i)
            }
        }
        return divisors
    }
}
*/*/
