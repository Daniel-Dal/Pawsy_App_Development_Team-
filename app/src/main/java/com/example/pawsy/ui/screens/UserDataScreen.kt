package com.example.pawsy.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pawsy.R
import com.example.pawsy.subirImagen
import com.example.pawsy.ui.components.ContinueBotton
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private data class UserData(
    val nombre: String = "",
    val correo: String = "",
    val fechaNacimiento: String = "",
    val urlImagen: String = ""
)

private suspend fun cargarUsuario(uid: String, base: UserData): UserData {
    val doc = FirebaseFirestore.getInstance()
        .collection("usuarios").document(uid).get().await()

    val fecha = doc.getTimestamp("fecha_nacimiento")?.toDate()?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota")
        }.format(it)
    }.orEmpty()

    return UserData(
        nombre = doc.getString("nombre").orEmpty().ifBlank { base.nombre },
        correo = doc.getString("correo").orEmpty().ifBlank { base.correo },
        fechaNacimiento = fecha,
        urlImagen = doc.getString("urlimagen").orEmpty()
    )
}

// OJO: es una suposición. Ajusta esta función a como guardan las mascotas.
private suspend fun cargarMascotas(uid: String): List<String> =
    FirebaseFirestore.getInstance()
        .collection("mascotas")
        .whereEqualTo("idDueno", uid)
        .get().await()
        .documents.mapNotNull { it.getString("nombre") }

@Composable
fun UserDataScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val firebaseUser = remember { FirebaseAuth.getInstance().currentUser }
    val uid = firebaseUser?.uid

    val base = remember {
        UserData(
            nombre = firebaseUser?.displayName.orEmpty(),
            correo = firebaseUser?.email.orEmpty()
        )
    }
    var usuario by remember { mutableStateOf(base) }
    var mascotas by remember { mutableStateOf<List<String>>(emptyList()) }
    var subiendo by remember { mutableStateOf(false) }

    val errorCargar = stringResource(R.string.usuario_error_cargar)
    val errorFoto = stringResource(R.string.usuario_error_foto)
    val fotoOk = stringResource(R.string.usuario_foto_actualizada)

    LaunchedEffect(uid) {
        if (uid == null) return@LaunchedEffect
        try {
            usuario = cargarUsuario(uid, base)
            mascotas = cargarMascotas(uid)
        } catch (e: Exception) {
            Toast.makeText(context, "$errorCargar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null || uid == null || subiendo) return@rememberLauncherForActivityResult
        scope.launch {
            subiendo = true
            try {
                val url = subirImagen(context = context, imagenUri = uri)
                FirebaseFirestore.getInstance()
                    .collection("usuarios").document(uid)
                    .set(mapOf("urlimagen" to url), SetOptions.merge())
                    .await()
                usuario = usuario.copy(urlImagen = url)
                Toast.makeText(context, fotoOk, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "$errorFoto: ${e.message}", Toast.LENGTH_LONG).show()
            }
            subiendo = false
        }
    }

    UserDataContent(
        usuario = usuario,
        mascotas = mascotas,
        subiendo = subiendo,
        onCambiarFoto = { picker.launch("image/*") }
    )
}

@Composable
private fun UserDataContent(
    usuario: UserData,
    mascotas: List<String>,
    subiendo: Boolean,
    onCambiarFoto: () -> Unit
) {
    PawsyTheme {
        val colors = MaterialTheme.pawsyColors

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.fondoPerfil)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.boton_datosUsuario),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textEditTextAgregarMascota,
                modifier = Modifier.padding(top = 48.dp)
            )

            // Foto de perfil
            val placeholder = painterResource(R.drawable.persona)
            AsyncImage(
                model = usuario.urlImagen.ifBlank { null },
                contentDescription = null,
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onCambiarFoto)
                    .align(Alignment.CenterHorizontally)
            )

            if (subiendo) LinearProgressIndicator(Modifier.fillMaxWidth())

            ContinueBotton(
                onClick = onCambiarFoto,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally),
                textColor = colors.textEditTextAgregarMascota,
                borderColor = colors.textEditTextAgregarMascota,
                borderWidth = 1.dp
            ) {
                Text(text = stringResource(R.string.usuario_cambiar_foto))
            }

            // Datos
            InfoField(stringResource(R.string.usuario_nombre), usuario.nombre)
            InfoField(stringResource(R.string.usuario_correo), usuario.correo)
            InfoField(stringResource(R.string.usuario_fecha_nacimiento), usuario.fechaNacimiento)

            // Mascotas (solo nombre)
            Text(
                text = stringResource(R.string.usuario_mis_mascotas),
                style = MaterialTheme.typography.labelLarge,
                color = colors.textEditTextAgregarMascota
            )

            if (mascotas.isEmpty()) {
                Text(
                    text = stringResource(R.string.usuario_sin_mascotas),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textEditTextAgregarMascota.copy(alpha = 0.7f)
                )
            } else {
                mascotas.forEach { nombre -> InfoBox(nombre) }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
        )
        Spacer(Modifier.height(6.dp))
        InfoBox(value.ifBlank { "—" })
    }
}

@Composable
private fun InfoBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.pawsyColors.recuadroInicioSesion)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.pawsyColors.blanco
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, showSystemUi = true)
@Composable
private fun UserDataPreview() {
    UserDataContent(
        usuario = UserData("Ana Gómez", "ana@correo.com", "14/03/1998", ""),
        mascotas = listOf("Luna", "Max"),
        subiendo = false,
        onCambiarFoto = {}
    )
}