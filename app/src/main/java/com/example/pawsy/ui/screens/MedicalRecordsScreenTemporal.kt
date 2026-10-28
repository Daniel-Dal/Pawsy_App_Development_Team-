package com.example.pawsy.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsy.R
import com.example.pawsy.data.MedicalRecordsStorage
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/** Documento clínico guardado dentro del arreglo "historias" de la mascota. */
data class HistoriaDocumento(
    val nombre: String,
    val path: String,
    val tamanoBytes: Long
)

/** Lee el arreglo "historias" de la mascota. Si no existe, devuelve lista vacía. */
private suspend fun cargarHistorias(mascotaId: String): List<HistoriaDocumento> {
    val doc = FirebaseFirestore.getInstance()
        .collection("mascotas")
        .document(mascotaId)
        .get()
        .await()

    val lista = doc.get("historias") as? List<*> ?: return emptyList()

    return lista.mapNotNull { item ->
        val mapa = item as? Map<*, *> ?: return@mapNotNull null
        val path = mapa["path"] as? String ?: return@mapNotNull null
        HistoriaDocumento(
            nombre = (mapa["nombre"] as? String).orEmpty()
                .ifBlank { path.substringAfterLast('/') },
            path = path,
            tamanoBytes = (mapa["tamano"] as? Number)?.toLong() ?: 0L
        )
    }
}

/** Agrega el documento al arreglo "historias"; si el arreglo no existe, lo crea. */
private suspend fun guardarHistoria(
    mascotaId: String,
    nombre: String,
    path: String,
    tamanoBytes: Long
) {
    val registro = hashMapOf(
        "nombre" to nombre,
        "path" to path,
        "tamano" to tamanoBytes,
        "fecha" to Timestamp.now()
    )

    FirebaseFirestore.getInstance()
        .collection("mascotas")
        .document(mascotaId)
        .update("historias", FieldValue.arrayUnion(registro))
        .await()
}

/** Nombre y tamaño del archivo elegido en el selector. */
private fun infoArchivo(context: Context, uri: Uri): Pair<String, Long> {
    var nombre = "documento"
    var tamano = 0L
    context.contentResolver.query(uri, null, null, null, null)?.use { c ->
        if (c.moveToFirst()) {
            val iNombre = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (iNombre >= 0) c.getString(iNombre)?.let { nombre = it }
            val iTamano = c.getColumnIndex(OpenableColumns.SIZE)
            if (iTamano >= 0 && !c.isNull(iTamano)) tamano = c.getLong(iTamano)
        }
    }
    return nombre to tamano
}

// Lógica: Firestore (lista) + Supabase (archivos)
@Composable
fun MedicalRecordsScreen(navController: NavController, mascotaId: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var documentos by remember { mutableStateOf<List<HistoriaDocumento>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    // Archivo elegido, pendiente de confirmar el nombre
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }
    var tamanoPendiente by remember { mutableStateOf(0L) }
    var nombreEditable by remember { mutableStateOf("") }

    fun toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    fun refresh() {
        if (mascotaId == null) return
        scope.launch {
            loading = true
            try {
                documentos = cargarHistorias(mascotaId)
            } catch (e: Exception) {
                toast("No se pudieron cargar los documentos: ${e.message}")
            } finally {
                loading = false
            }
        }
    }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val (nombre, tamano) = infoArchivo(context, uri)
        uriPendiente = uri
        tamanoPendiente = tamano
        nombreEditable = nombre.substringBeforeLast('.', nombre)   // sin extensión
    }

    LaunchedEffect(mascotaId) { refresh() }

    // Diálogo para cambiar el nombre antes de subir
    val uriParaSubir = uriPendiente
    if (uriParaSubir != null && mascotaId != null) {
        AlertDialog(
            onDismissRequest = { uriPendiente = null },
            title = { Text("Nombre del documento") },
            text = {
                OutlinedTextField(
                    value = nombreEditable,
                    onValueChange = { nombreEditable = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    enabled = nombreEditable.isNotBlank(),
                    onClick = {
                        val nombre = nombreEditable.trim()
                        val tamano = tamanoPendiente
                        uriPendiente = null

                        scope.launch {
                            loading = true
                            try {
                                val path = MedicalRecordsStorage
                                    .upload(context, uriParaSubir)
                                    .getOrThrow()
                                guardarHistoria(mascotaId, nombre, path, tamano)
                                toast("Documento guardado")
                                documentos = cargarHistorias(mascotaId)
                            } catch (e: Exception) {
                                toast(e.message ?: "Error al guardar el documento")
                            } finally {
                                loading = false
                            }
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { uriPendiente = null }) { Text("Cancelar") }
            }
        )
    }

    MedicalRecordsContent(
        documentos = documentos,
        loading = loading,
        puedeSubir = mascotaId != null,
        onUploadClick = {
            picker.launch(arrayOf("image/jpeg", "image/png", "application/pdf"))
        },
        onFileClick = { doc ->
            scope.launch {
                MedicalRecordsStorage.getSignedUrl(doc.path)
                    .onSuccess {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                    .onFailure { toast(it.message ?: "No se pudo abrir el documento") }
            }
        }
    )
}

// UI pura: sin red, con vista previa
@Composable
fun MedicalRecordsContent(
    documentos: List<HistoriaDocumento>,
    loading: Boolean,
    puedeSubir: Boolean,
    onUploadClick: () -> Unit,
    onFileClick: (HistoriaDocumento) -> Unit
) {
    PawsyTheme {
        val textColor = MaterialTheme.pawsyColors.textEditTextAgregarMascota

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.fondoPerfil)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.titulo_historias_medicas),
                style = MaterialTheme.typography.titleLarge,
                color = textColor,
                modifier = Modifier.padding(top = 48.dp, bottom = 20.dp)
            )

            Button(
                onClick = onUploadClick,
                enabled = !loading && puedeSubir,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
                    contentColor = MaterialTheme.pawsyColors.blanco
                )
            ) {
                Text(
                    text = stringResource(id = R.string.boton_subir_historia),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.lista_historias),
                style = MaterialTheme.typography.labelSmall,
                color = textColor
            )

            Spacer(Modifier.height(8.dp))

            if (documentos.isEmpty() && !loading) {
                Text(
                    text = if (puedeSubir) {
                        "Aún no hay documentos clínicos para esta mascota."
                    } else {
                        "No se pudo identificar la mascota."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(documentos) { doc ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onFileClick(doc) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(68.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.pawsyColors.fondoPerfil,
                                contentColor = textColor
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = doc.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                if (doc.tamanoBytes > 0) {
                                    Text(
                                        text = "${doc.tamanoBytes / 1024} KB",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = textColor.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            color = textColor.copy(alpha = 0.25f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, showSystemUi = true)
@Composable
fun MedicalRecordsPreview() {
    MedicalRecordsContent(
        documentos = listOf(
            HistoriaDocumento("Vacunas 2026", "demo/1_vacunas.pdf", 238_112),
            HistoriaDocumento("Radiografía", "demo/2_radiografia.png", 1_450_000),
            HistoriaDocumento("Examen de sangre", "demo/3_examen.jpg", 820_300)
        ),
        loading = false,
        puedeSubir = true,
        onUploadClick = {},
        onFileClick = {}
    )
}