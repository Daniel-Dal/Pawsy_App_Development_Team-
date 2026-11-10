package com.example.pawsy.ui.screens

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.R
import com.example.pawsy.data.AgendaEventos
import com.example.pawsy.data.CalendarioDispositivo
import com.example.pawsy.model.Categoria
import com.example.pawsy.model.Evento
import com.example.pawsy.model.FechaEvento
import com.example.pawsy.model.MesAnio
import com.example.pawsy.model.Recurrencia
import com.example.pawsy.model.TipoFrecuencia
import com.example.pawsy.model.aFecha
import com.example.pawsy.model.aLocal
import com.example.pawsy.model.ocurreEn
import com.example.pawsy.ui.components.CalendarioMensual
import com.example.pawsy.ui.components.PetSideMenuOverlay
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset

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
    pawImage: Painter = painterResource(R.drawable.ic_paw_outline),
    mascotaId: String? = null
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

    // Días del mes visible que tienen al menos un evento (incluye repeticiones)
    val diasConEventos = (1..YearMonth.of(mesVisible.anio, mesVisible.mes).lengthOfMonth())
        .map { mesVisible.dia(it) }
        .filter { f -> eventos.any { it.ocurreEn(f) } }
        .toSet()

    val eventosDelDia = eventos
        .filter { it.ocurreEn(diaSeleccionado) }
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

                        // Título en una sola línea horizontal
                        Text(
                            text = "Eventos · ${diaSeleccionado.etiqueta()}",
                            color = colors.textEditTextAgregarMascota,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 16.sp,
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Lista de eventos ordenada, con espacio entre filas
                        if (eventosDelDia.isEmpty()) {
                            FilaEvento(titulo = "Sin eventos este día")
                        } else {
                            eventosDelDia.forEachIndexed { indice, evento ->
                                if (indice > 0) Spacer(modifier = Modifier.height(8.dp))
                                FilaEvento(
                                    titulo = "${evento.categoria.emoji} ${evento.titulo}",
                                    hora = evento.hora,
                                    subtitulo = evento.recurrencia.resumen(),
                                    acento = evento.categoria.color,
                                    onEliminar = { idEventoAEliminar = evento.id }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Botón de agregar en la parte inferior, ancho completo
                        OutlinedButton(
                            onClick = { mostrarDialogo = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, colors.textEditTextAgregarMascota),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.textEditTextAgregarMascota
                            )
                        ) {
                            Text(
                                text = "+ Agregar evento",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_paw_menu),
                            contentDescription = "Pawsy",
                            modifier = Modifier
                                .size(70.dp)
                                .clickable(onClick = { menuExpanded = !menuExpanded })
                        )
                    }
                }
            }

            PetSideMenuOverlay(
                navController = navController,
                visible = menuExpanded,
                onDismiss = { menuExpanded = false },
                mascotaId = mascotaId
            )
        }

        if (mostrarDialogo) {
            DialogoNuevoEvento(
                fecha = diaSeleccionado,
                onDismiss = { mostrarDialogo = false },
                onConfirm = { nuevo ->
                    onAgregarEvento(nuevo)
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
    subtitulo: String = "",
    acento: Color? = null,
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
        if (acento != null) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(acento)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = titulo,
                color = colors.blanco,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitulo.isNotBlank()) {
                Text(
                    text = subtitulo,
                    color = colors.blanco.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
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

@Composable
private fun ChipColor(
    texto: String,
    seleccionado: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto, maxLines = 1) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun DialogoNuevoEvento(
    fecha: FechaEvento,
    onDismiss: () -> Unit,
    onConfirm: (Evento) -> Unit
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf(Categoria.CITA_SALUD) }
    var tipo by rememberSaveable { mutableStateOf(TipoFrecuencia.UNA_VEZ) }
    var conHora by rememberSaveable { mutableStateOf(false) }
    var cadaTexto by rememberSaveable { mutableStateOf("1") }
    var dias by remember { mutableStateOf(setOf<Int>()) }
    var fin by remember { mutableStateOf<LocalDate?>(null) }
    var eligiendoFin by remember { mutableStateOf(false) }

    // Selector numérico de 24 h: no hace falta escribir ":"
    val horaState = rememberTimePickerState(initialHour = 9, initialMinute = 0, is24Hour = true)
    val fechaState = rememberDatePickerState()

    val inicio = fecha.aLocal()
    val cada = cadaTexto.toIntOrNull() ?: 0
    val finObligatorio = categoria == Categoria.MEDICAMENTO && tipo != TipoFrecuencia.UNA_VEZ
    val horaObligatoria = tipo == TipoFrecuencia.CADA_N_HORAS

    val valido = titulo.isNotBlank() &&
            (tipo != TipoFrecuencia.DIAS_SEMANA || dias.isNotEmpty()) &&
            ((tipo != TipoFrecuencia.CADA_N_DIAS && tipo != TipoFrecuencia.CADA_N_HORAS) || cada >= 1) &&
            (!finObligatorio || (fin != null && !fin!!.isBefore(inicio)))

    fun elegir(c: Categoria) {
        categoria = c
        // Frecuencia sugerida según la etiqueta; el usuario puede cambiarla
        tipo = if (c == Categoria.ALIMENTACION || c == Categoria.MEDICAMENTO)
            TipoFrecuencia.DIARIA else TipoFrecuencia.UNA_VEZ
        if (tipo == TipoFrecuencia.UNA_VEZ) fin = null
    }

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

        // ---------- Etiqueta ----------
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Etiqueta",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(Categoria.CITA_SALUD, Categoria.ALIMENTACION, Categoria.PASEO).forEach { c ->
                ChipColor("${c.emoji} ${c.grupo}", categoria.grupo == c.grupo, c.color) { elegir(c) }
            }
        }
        if (categoria.grupo == "Salud") {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(Categoria.CITA_SALUD, Categoria.MEDICAMENTO).forEach { c ->
                    ChipColor("${c.emoji} ${c.etiqueta}", categoria == c, c.color) { elegir(c) }
                }
            }
        }

        // ---------- Frecuencia ----------
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Frecuencia",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TipoFrecuencia.values().forEach { t ->
                ChipColor(t.texto, tipo == t, categoria.color) { tipo = t }
            }
        }

        if (tipo == TipoFrecuencia.DIAS_SEMANA) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                listOf("L", "M", "X", "J", "V", "S", "D").forEachIndexed { i, letra ->
                    val n = i + 1
                    ChipColor(letra, n in dias, categoria.color) {
                        dias = if (n in dias) dias - n else dias + n
                    }
                }
            }
        }

        if (tipo == TipoFrecuencia.CADA_N_DIAS || tipo == TipoFrecuencia.CADA_N_HORAS) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cadaTexto,
                onValueChange = { cadaTexto = it.filter(Char::isDigit).take(2) },
                label = {
                    Text(
                        if (tipo == TipoFrecuencia.CADA_N_HORAS) "Cada cuántas horas"
                        else "Cada cuántos días"
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ---------- Fecha de finalización ----------
        if (tipo != TipoFrecuencia.UNA_VEZ) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { eligiendoFin = !eligiendoFin },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = fin?.let { "🏁 Termina: ${it.dayOfMonth}/${it.monthValue}/${it.year}" }
                        ?: if (finObligatorio) "🏁 Fecha de fin del tratamiento *"
                        else "🏁 Sin fecha de fin (opcional)",
                    fontSize = 12.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
            if (eligiendoFin) {
                DatePicker(state = fechaState, showModeToggle = false)
                TextButton(onClick = {
                    fechaState.selectedDateMillis?.let {
                        fin = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    eligiendoFin = false
                }) { Text("Usar esta fecha") }
            }
            if (fin != null && !finObligatorio) {
                TextButton(onClick = { fin = null }) { Text("Quitar fecha de fin") }
            }
            if (finObligatorio && fin == null) {
                Text(
                    text = "El medicamento necesita fecha de fin.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        // ---------- Hora ----------
        Spacer(modifier = Modifier.height(8.dp))
        if (conHora || horaObligatoria) {
            Text(
                text = if (horaObligatoria) "Hora de la primera toma (24 h)" else "Hora (24 h)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimeInput(
                state = horaState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if (!horaObligatoria) {
                TextButton(onClick = { conHora = false }) { Text("Quitar hora") }
            }
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
                enabled = valido,
                onClick = {
                    val hora = if (conHora || horaObligatoria) {
                        "%02d:%02d".format(horaState.hour, horaState.minute)
                    } else ""
                    onConfirm(
                        Evento(
                            fecha,
                            titulo.trim(),
                            hora,
                            categoria = categoria,
                            recurrencia = Recurrencia(
                                tipo = tipo,
                                diasSemana = dias,
                                cada = cada.coerceAtLeast(1),
                                fin = fin?.aFecha()
                            )
                        )
                    )
                }
            ) { Text("Agregar") }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 700)
@Composable
private fun RegistroPaseoActividadPreview() {
    val navController = rememberNavController()
    // Lista propia del preview, para que agregar/quitar sí se vea aquí.
    val eventosDemo = remember {
        mutableStateListOf(
            Evento(
                FechaEvento.hoy(), "Paseo en el parque", "07:00",
                categoria = Categoria.PASEO,
                recurrencia = Recurrencia(TipoFrecuencia.DIARIA)
            ),
            Evento(
                FechaEvento.hoy(), "Antibiótico", "08:00",
                categoria = Categoria.MEDICAMENTO,
                recurrencia = Recurrencia(
                    TipoFrecuencia.CADA_N_HORAS, cada = 8,
                    fin = MesAnio.actual().dia(28)
                )
            ),
            Evento(
                FechaEvento.hoy(), "Croquetas", "18:00",
                categoria = Categoria.ALIMENTACION,
                recurrencia = Recurrencia(TipoFrecuencia.DIARIA)
            ),
            Evento(
                MesAnio.actual().dia(20), "Cita veterinaria",
                categoria = Categoria.CITA_SALUD
            )
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