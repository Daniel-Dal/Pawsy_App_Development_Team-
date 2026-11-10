package com.example.pawsy.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.navigation.NavController
import com.example.pawsy.ui.screens.MascotaSeleccionada
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.pawsyColors

@Composable
fun PetSideMenu(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    menuItems: List<String> = listOf(
        "Perfil mascota",
        "Perfil usuario",
        "Paseos y actividad",
        "Recomendaciones",
        "Veterinarias cercanas",
        "Reconocimiento visual",
        "Eventos",
        "Salir"
    )
) {
    val colors = MaterialTheme.pawsyColors

    var dragAccumulated by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.72f)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    dragAccumulated = (dragAccumulated + delta).coerceAtLeast(0f)
                },
                onDragStopped = {
                    if (dragAccumulated > 150f) {
                        onDismiss()
                    }
                    dragAccumulated = 0f
                }
            )
            .clip(RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp))
            .background(colors.recuadroInicioSesion)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.recuadroInicioSesion),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_paw_outline),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Botón para cerrar el menú
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar menú",
                        tint = colors.blanco,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            menuItems.forEach { item ->
                Text(
                    text = item,
                    color = colors.blanco,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                onItemClick(item)
                                onDismiss()
                            }
                        )
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun BoxScope.PetSideMenuOverlay(
    navController: NavController,
    visible: Boolean,
    onDismiss: () -> Unit,
    mascotaId: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BackHandler(enabled = visible, onBack = onDismiss)

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.matchParentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                )
        )
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        modifier = Modifier.align(Alignment.CenterEnd)
    ) {
        PetSideMenu(
            onItemClick = { item ->
                when (item) {
                    "Perfil mascota" -> {
                        val id = mascotaId ?: MascotaSeleccionada.id
                        if (id != null) navController.navigate("petProfile/$id")
                        else navController.navigate("myPets")
                    }
                    "Perfil usuario" -> navController.navigate("userProfile")
                    "Paseos y actividad" -> navController.navigate("pya")
                    "Recomendaciones" -> navController.navigate("recom")
                    "Veterinarias cercanas" -> navController.navigate("nearbyVets")
                    "Reconocimiento visual" -> navController.navigate("food")
                    "Eventos" -> navController.navigate("eventos")
                    "Salir" -> {
                        FirebaseAuth.getInstance().signOut()
                        MascotaSeleccionada.id = null
                        // Olvida también la cuenta de Google seleccionada
                        scope.launch {
                            try {
                                CredentialManager.create(context)
                                    .clearCredentialState(ClearCredentialStateRequest())
                            } catch (_: Exception) {
                            }
                        }
                        navController.navigate("menu") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            },
            onDismiss = onDismiss
        )
    }
}
