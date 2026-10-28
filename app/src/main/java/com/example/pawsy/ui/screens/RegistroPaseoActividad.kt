package com.example.pawsy.ui.screens

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.R
import com.example.pawsy.data.AgendaEventos
import com.example.pawsy.data.CalendarioDispositivo
import com.example.pawsy.model.Evento
import com.example.pawsy.model.FechaEvento
import com.example.pawsy.model.MesAnio
import com.example.pawsy.ui.components.CalendarioMensual
import com.example.pawsy.ui.components.PetSideMenu
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors


private val FechaSaver = listSaver(
    save = { listOf(it.anio, it.mes, it.dia) },
    restore = { FechaEvento(it[0], it[1], it[2]) }
)
private val MesSaver = listSaver(
    save = { listOf(it.anio, it.mes) },
    restore = { MesAnio(it[0], it[1]) }
)

@Composable
fun RegistroPaseoActividad(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    sincronizarConCalendario: Boolean = true,
    eventos: List<Evento> = AgendaEventos.eventos,
    onAgregarEvento: (Evento) -> Unit = { AgendaEventos.agregar(context, it) },
    onEliminarEvento: (Evento) -> Unit = { AgendaEventos.eliminar(context, it) },
    walkIcon: Painter = painterResource(R.drawable.pawsy_r_p_a_app),
    pawImage: Painter = painterResource(R.drawable.ic_paw_outline)
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var mesVisible by rememberSaveable(stateSaver = MesSaver) { mutableStateOf(MesAnio.actual()) }
    var diaSeleccionado by rememberSaveable(stateSaver = FechaSaver) {
        mutableStateOf(FechaEvento.hoy())
    }
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
    var idEventoAEliminar by rememberSaveable { mutableStateOf<String?>(null) }

    val colors = MaterialTheme.pawsyColors

    if (sincronizarConCalendario) {
        PermisoYCargaDeCalendario(context)
    }

    val diasConEventos = eventos.map { it.fecha }.toSet()
    val eventosDelDia = eventos
        .filter { it.fecha == diaSeleccionado }
        .sortedWith(compareBy({ it.hora.isBlank() }, { it.hora }))

    fun cambiarMes(nuevo: MesAnio) {
        mesVisible = nuevo
        diaSeleccionado = if (nuevo == MesAnio.actual()) FechaEvento.hoy() else nuevo.dia(1)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.fondoPerfil)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                        .background(colors.recuadroInicioSesion)
                        .padding(horizontal = 24.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Paseos y\nactividad",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Image(
                            painter = pawImage,
                            contentDescription = "Abrir menú",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable(onClick = { menuExpanded = !menuExpanded })
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    // Contenido desplazable: nada se corta en pantallas pequeñas
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Registro paseos",
                            color = colors.textEditTextAgregarMascota,
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        CalendarioMensual(
                            mes = mesVisible,
                            seleccionado = diaSeleccionado,
                            diasConEventos = diasConEventos,
                            onMesAnterior = { cambiarMes(mesVisible.masMeses(-1)) },
                            onMesSiguiente = { cambiarMes(mesVisible.masMeses(1)) },
                            onDiaClick = { diaSeleccionado = it },
                            encabezadoExtra = {
                                Image(
                                    painter = walkIcon,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(colors.negro),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Eventos · ${diaSeleccionado.etiqueta()}",
                                color = colors.textEditTextAgregarMascota,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "+ Agregar evento",
                                color = colors.textEditTextAgregarMascota,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(0.5.dp))
                                    .clickable(
                                        onClickLabel = "Agregar evento",
                                        onClick = { mostrarDialogo = true }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (eventosDelDia.isEmpty()) {
                            FilaEvento(titulo = "Sin eventos este día")
                        } else {
                            eventosDelDia.forEachIndexed { indice, evento ->
                                if (indice > 0) Spacer(modifier = Modifier.height(8.dp))
                                FilaEvento(
                                    titulo = evento.titulo,
                                    hora = evento.hora,
                                    onEliminar = { idEventoAEliminar = evento.id }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = pawImage,
                            contentDescription = "Pawsy",
                            modifier = Modifier
                                .size(44.dp)
                                .clickable(onClick = { menuExpanded = !menuExpanded })
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = menuExpanded,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                PetSideMenu(
                    onItemClick = { item ->
                        when (item) {
                            "Perfil mascota" -> navController.navigate("petProfile")
                            "Perfil usuario" -> navController.navigate("userProfile")
                            "Recomendaciones" -> navController.navigate("recom/g0it5yJqSri9ncX7Uonj")

                        }
                    },
                    onDismiss = { menuExpanded = false }
                )
            }
        }

        // Paneles superpuestos (dibujados dentro de la pantalla, sin ventana aparte)
        if (mostrarDialogo) {
            DialogoNuevoEvento(
                fecha = diaSeleccionado,
                onDismiss = { mostrarDialogo = false },
                onConfirm = { titulo, hora ->
                    onAgregarEvento(Evento(diaSeleccionado, titulo.trim(), hora.trim()))
                    mostrarDialogo = false
                }
            )
        }

        val eventoAEliminar = eventos.firstOrNull { it.id == idEventoAEliminar }
        if (eventoAEliminar != null) {
            DialogoConfirmarEliminar(
                evento = eventoAEliminar,
                onDismiss = { idEventoAEliminar = null },
                onConfirm = {
                    onEliminarEvento(eventoAEliminar)
                    idEventoAEliminar = null
                }
            )
        }
    }

}

@Composable
private fun PermisoYCargaDeCalendario(context: Context) {
    val solicitarPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (CalendarioDispositivo.tienePermisos(context)) {
            AgendaEventos.cargarDesdeCalendario(context)
        }
    }

    LaunchedEffect(Unit) {
        if (CalendarioDispositivo.tienePermisos(context)) {
            AgendaEventos.cargarDesdeCalendario(context)
        } else {
            solicitarPermisos.launch(
                arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
            )
        }
    }
}

@Composable
private fun PanelModal(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .verticalScroll(rememberScrollState())
                // Absorbe los toques para que no cierren el panel
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {}
                )
                .padding(24.dp),
            content = content
        )
    }
}

@Composable
private fun DialogoConfirmarEliminar(
    evento: Evento,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    PanelModal(onDismiss = onDismiss) {
        Text(
            text = "Quitar evento",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "¿Está seguro que quiere quitar el evento \"${evento.titulo}\"?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
            TextButton(onClick = onConfirm) { Text("Quitar") }
        }
    }
}

@Composable
private fun FilaEvento(
    titulo: String,
    hora: String = "",
    onEliminar: (() -> Unit)? = null
) {
    val colors = MaterialTheme.pawsyColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(colors.recuadroInicioSesion)
            .padding(start = 20.dp, end = if (onEliminar != null) 8.dp else 20.dp)
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            color = colors.blanco,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
        )
        if (hora.isNotBlank()) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = hora,
                color = colors.blanco.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (onEliminar != null) {
            IconButton(onClick = onEliminar, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Eliminar evento",
                    tint = colors.blanco
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoNuevoEvento(
    fecha: FechaEvento,
    onDismiss: () -> Unit,
    onConfirm: (titulo: String, hora: String) -> Unit
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var conHora by rememberSaveable { mutableStateOf(false) }
    // Selector numérico de 24 h: no hace falta escribir ":"
    val horaState = rememberTimePickerState(initialHour = 9, initialMinute = 0, is24Hour = true)

    PanelModal(onDismiss = onDismiss) {
        Text(
            text = "Nuevo evento",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = fecha.etiqueta(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Evento (ej. Cita veterinaria)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (conHora) {
            Text(
                text = "Hora (formato 24 h)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimeInput(
                state = horaState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            TextButton(onClick = { conHora = false }) { Text("Quitar hora") }
        } else {
            TextButton(onClick = { conHora = true }) { Text("+ Agregar hora (opcional)") }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
            TextButton(
                onClick = {
                    val hora = if (conHora) {
                        "%02d:%02d".format(horaState.hour, horaState.minute)
                    } else ""
                    onConfirm(titulo, hora)
                },
                enabled = titulo.isNotBlank()
            ) { Text("Agregar") }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 700)
@Composable
private fun RegistroPaseoActividadPreview() {
    val navController = rememberNavController()
    // Lista propia del preview, para que agregar/quitar sí se vea aquí.
    // (Antes los botones escribían en AgendaEventos, que el preview no mostraba.)
    val eventosDemo = remember {
        mutableStateListOf(
            Evento(FechaEvento.hoy(), ".", "."),
            Evento(FechaEvento.hoy(), "."),
            Evento(MesAnio.actual().dia(20), ".")
        )
    }
    PawsyTheme {
        RegistroPaseoActividad(
            navController = navController,
            sincronizarConCalendario = false,
            eventos = eventosDemo,
            onAgregarEvento = { eventosDemo.add(it) },
            onEliminarEvento = { evento -> eventosDemo.removeAll { it.id == evento.id } }
        )
    }
}
