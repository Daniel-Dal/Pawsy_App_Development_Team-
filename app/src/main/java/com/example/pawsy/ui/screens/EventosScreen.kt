@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pawsy.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors

@Composable
fun EventosScreen(
    navController: NavController,
    vm: EventosViewModel = viewModel()
) {
    val context = LocalContext.current

    PawsyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.editTextAgregarMascota)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 35.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Eventos para mascotas",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.pawsyColors.blanco
                    )
                    Image(
                        painter = painterResource(id = R.drawable.pawsy_paw_white),
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(105f)
                            .size(56.dp)
                    )
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(MaterialTheme.pawsyColors.fondoPerfil)
                ) {
                    Spacer(Modifier.height(16.dp))


                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            ChipFiltro("Todos", vm.tipoSeleccionado == null) {
                                vm.tipoSeleccionado = null
                            }
                        }
                        items(TIPOS_EVENTO) { (clave, etiqueta) ->
                            ChipFiltro(etiqueta, vm.tipoSeleccionado == clave) {
                                vm.tipoSeleccionado = clave
                            }
                        }
                    }


                    val ciudades = vm.ciudades
                    if (ciudades.size > 1) {
                        Spacer(Modifier.height(8.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                ChipFiltro("📍 Todas", vm.ciudadSeleccionada == null) {
                                    vm.ciudadSeleccionada = null
                                }
                            }
                            items(ciudades) { ciudad ->
                                ChipFiltro(ciudad, vm.ciudadSeleccionada == ciudad) {
                                    vm.ciudadSeleccionada = ciudad
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))


                    val eventos = vm.eventos
                    when {
                        vm.cargando -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.pawsyColors.editTextAgregarMascota)
                        }

                        vm.error != null -> MensajeVacio("No pudimos cargar los eventos 😿\n${vm.error}")

                        eventos.isEmpty() -> MensajeVacio("Aún no hay eventos próximos 🐾\nVuelve pronto, ¡habrá novedades!")

                        else -> LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(eventos, key = { it.id }) { evento ->
                                EventoCard(evento) { vm.abrirDetalle(evento) }
                            }
                        }
                    }
                }
            }
        }


        val seleccionado = vm.eventoSeleccionado
        if (seleccionado != null) {
            ModalBottomSheet(
                onDismissRequest = { vm.cerrarDetalle() },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                DetalleEvento(seleccionado, vm, context)
            }
        }
    }
}


@Composable
private fun DetalleEvento(evento: Evento, vm: EventosViewModel, context: Context) {
    val naranja = MaterialTheme.pawsyColors.editTextAgregarMascota
    val botonShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AsyncImage(
            model = evento.imagenUrl,
            contentDescription = evento.titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFEFEFEF))
        )


        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Etiqueta(etiquetaTipo(evento.tipo))
            if (evento.destacado) Etiqueta("⭐ Destacado")
        }

        Text(
            text = evento.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        InfoFila("📅", evento.fecha.formatear())
        InfoFila("📍", listOf(evento.direccion, evento.ciudad).filter { it.isNotBlank() }.joinToString(", "))
        if (evento.organizador.isNotBlank()) InfoFila("🐾", "Organiza: ${evento.organizador}")

        if (evento.descripcion.isNotBlank()) {
            Text(
                text = evento.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }


        Button(
            onClick = { abrirMapa(context, evento) },
            shape = botonShape,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = naranja, contentColor = Color.White)
        ) { Text("📍 Cómo llegar", maxLines = 1) }

        Button(
            onClick = {
                vm.alternarGuardado {
                    Toast.makeText(context, "Inicia sesión para guardar eventos", Toast.LENGTH_SHORT).show()
                }
            },
            shape = botonShape,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (vm.guardado) Color(0xFF3A2E2A) else naranja,
                contentColor = Color.White
            )
        ) { Text(if (vm.guardado) "❤️ Guardado" else "🤍 Me interesa", maxLines = 1) }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { agregarAlCalendario(context, evento) },
                shape = botonShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) { Text("🗓️ Calendario", maxLines = 1) }

            OutlinedButton(
                onClick = { compartir(context, evento) },
                shape = botonShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) { Text("↗ Compartir", maxLines = 1) }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun InfoFila(emoji: String, texto: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(emoji)
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}


@Composable
private fun ChipFiltro(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto, maxLines = 1) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = Color.Black,
            selectedContainerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
            selectedLabelColor = Color.White
        )
    )
}

@Composable
private fun EventoCard(evento: Evento, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box {
                AsyncImage(
                    model = evento.imagenUrl,
                    contentDescription = evento.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color(0xFFEFEFEF))
                )

                if (evento.destacado) {
                    Etiqueta(
                        texto = "⭐ Destacado",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    )
                }
                Etiqueta(
                    texto = etiquetaTipo(evento.tipo),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                )
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = evento.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "📅 ${evento.fecha.formatear()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Text(
                    text = "📍 ${evento.ciudad}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun Etiqueta(texto: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.pawsyColors.editTextAgregarMascota
    ) {
        Text(
            text = texto,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun MensajeVacio(mensaje: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = mensaje,
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}


private fun lanzar(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No hay una app disponible para esta acción", Toast.LENGTH_SHORT).show()
    }
}

private fun abrirMapa(context: Context, e: Evento) {
    val uri = if (e.lat != null && e.lng != null) {
        Uri.parse("geo:${e.lat},${e.lng}?q=${e.lat},${e.lng}(${Uri.encode(e.titulo)})")
    } else {
        Uri.parse("geo:0,0?q=${Uri.encode("${e.direccion}, ${e.ciudad}")}")
    }
    lanzar(context, Intent(Intent.ACTION_VIEW, uri))
}

private fun agregarAlCalendario(context: Context, e: Evento) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, e.titulo)
        putExtra(CalendarContract.Events.DESCRIPTION, e.descripcion)
        putExtra(CalendarContract.Events.EVENT_LOCATION, "${e.direccion}, ${e.ciudad}")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, e.fecha.toDate().time)
    }
    lanzar(context, intent)
}

private fun compartir(context: Context, e: Evento) {
    val texto = "¡Mira este evento en Pawsy! 🐾\n${e.titulo}\n📅 ${e.fecha.formatear()}\n📍 ${e.direccion}, ${e.ciudad}"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, texto)
    }
    lanzar(context, Intent.createChooser(intent, "Compartir evento"))
}
