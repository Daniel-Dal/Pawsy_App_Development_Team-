package com.example.pawsy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Paleta de colores del formulario de perfil de mascota.
 * Si más adelante quieres centralizarla, muévela a ui/theme/Color.kt.
 */
object PawsyColors {
    val Background = Color(0xFFFCF3E7)
    val Primary = Color(0xFFF3855A)      // Naranja de los inputs y el ícono
    val TitleColor = Color(0xFFF07A52)   // Coral del título
    val LabelColor = Color(0xFFF3A98A)   // Coral claro de las etiquetas
    val FieldText = Color.White
    val IconCircleBg = Color.White
}

/**
 * Modelo de solo lectura con los datos de la mascota.
 * Esta pantalla NO captura datos: los recibe ya listos desde donde sea
 * que los hayas guardado (ViewModel, Room, API, argumentos de navegación, etc.)
 */
data class PetProfile(
    val nombre: String = "",
    val especie: String = "",
    val edad: String = "",
    val raza: String = "",
    val peso: String = ""
)

/**
 * Pantalla de solo lectura: muestra el perfil de una mascota.
 * La captura de datos ocurre en OTRA pantalla (ej. un formulario de registro);
 * aquí solo se recibe [petProfile] ya resuelto y se despliega.
 *
 * Ejemplo de uso con un ViewModel:
 *   val pet by viewModel.selectedPet.collectAsState()
 *   AddPetScreen(petProfile = pet)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    petProfile: PetProfile = PetProfile(),
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PawsyColors.Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {

                // Encabezado: título + ícono de huella
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Perfil\nmascota",
                        color = PawsyColors.TitleColor,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    PawIconBadge()
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Campos de solo lectura: muestran los datos recibidos en petProfile
                PetProfileField(label = "Nombre", value = petProfile.nombre)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Especie", value = petProfile.especie)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Edad", value = petProfile.edad)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Raza", value = petProfile.raza)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Peso", value = petProfile.peso)

                Spacer(modifier = Modifier.weight(1f))

                // Botón flotante inferior derecho (huella) — ej. para ir a editar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    PawSubmitButton(onClick = onEditClick)
                }
            }

            // Pestaña que despliega el menú lateral (marcada en azul en el mockup)
            MenuHandle(
                expanded = menuExpanded,
                onClick = { menuExpanded = !menuExpanded },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 176.dp)
            )
        }

        // Menú lateral desplegable. Sin funcionalidad todavía: solo muestra las opciones.
        AnimatedVisibility(
            visible = menuExpanded,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            PetSideMenu(onItemClick = { menuExpanded = false })
        }
    }
}

/**
 * Pestaña/manija en el borde derecho de la pantalla (círculo azul del mockup).
 * Al tocarla, abre o cierra el menú lateral.
 */
@Composable
private fun MenuHandle(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(14.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            .background(if (expanded) PawsyColors.Primary else PawsyColors.Primary.copy(alpha = 0.35f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.8f))
        )
    }
}

/**
 * Menú lateral con el listado de secciones de la app.
 * Por ahora es solo visual: onItemClick únicamente cierra el panel,
 * sin navegar a ninguna pantalla todavía.
 */
@Composable
private fun PetSideMenu(
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        "Perfil mascota",
        "Alimentación",
        "Salud",
        "Paseos y actividad",
        "Recomendaciones",
        "Reconocimiento visual",
        "Configuración",
        "Salir"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.72f)
            .clip(RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp))
            .background(PawsyColors.Primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PawsyColors.IconCircleBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = null,
                    tint = PawsyColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            menuItems.forEach { item ->
                Text(
                    text = item,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onItemClick
                        )
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}

/**
 * Campo de SOLO LECTURA: mantiene la píldora naranja del diseño,
 * pero muestra texto fijo en vez de un TextField editable.
 */
@Composable
private fun PetProfileField(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = PawsyColors.LabelColor,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(PawsyColors.Primary)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = value.ifBlank { "—" },
                color = PawsyColors.FieldText,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun PawIconBadge() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(PawsyColors.IconCircleBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Pets,
            contentDescription = "Pawsy",
            tint = PawsyColors.Primary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun PawSubmitButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(PawsyColors.IconCircleBg)
            .padding(2.dp)
            .clip(CircleShape)
            .background(PawsyColors.Primary),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.Pets,
                contentDescription = "Guardar perfil",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 700, name = "Cerrado")
@Composable
fun AddPetScreenPreview() {
    MaterialTheme {
        AddPetScreen()
    }
}

