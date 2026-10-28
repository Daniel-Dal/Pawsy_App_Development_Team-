package com.example.pawsy.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsy.R
import com.example.pawsy.data.MedicalRecordsStorage
import com.example.pawsy.data.RecordFile
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import kotlinx.coroutines.launch

// Logic: talks to Supabase
@Composable
fun MedicalRecordsScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var files by remember { mutableStateOf<List<RecordFile>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    fun toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    fun refresh() = scope.launch {
        loading = true
        MedicalRecordsStorage.list()
            .onSuccess { files = it }
            .onFailure { toast(it.message ?: "Error loading files") }
        loading = false
    }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            loading = true
            MedicalRecordsStorage.upload(context, uri)
                .onSuccess { toast("Uploaded"); refresh() }
                .onFailure { toast(it.message ?: "Upload error") }
            loading = false
        }
    }

    LaunchedEffect(Unit) { refresh() }

    MedicalRecordsContent(
        files = files,
        loading = loading,
        onUploadClick = {
            picker.launch(arrayOf("image/jpeg", "image/png", "application/pdf"))
        },
        onFileClick = { file ->
            scope.launch {
                MedicalRecordsStorage.getSignedUrl(file.path)
                    .onSuccess {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                    .onFailure { toast(it.message ?: "Error opening file") }
            }
        }
    )
}

// Pure UI: no network, previewable
@Composable
fun MedicalRecordsContent(
    files: List<RecordFile>,
    loading: Boolean,
    onUploadClick: () -> Unit,
    onFileClick: (RecordFile) -> Unit
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
                enabled = !loading,
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

            LazyColumn(Modifier.weight(1f)) {
                items(files) { file ->
                    ListItem(
                        headlineContent = { Text(file.name) },
                        supportingContent = { Text("${file.sizeBytes / 1024} KB") },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                            headlineColor = textColor,
                            supportingColor = textColor.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.clickable { onFileClick(file) }
                    )
                    HorizontalDivider(color = textColor.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, showSystemUi = true)
@Composable
fun MedicalRecordsPreview() {
    MedicalRecordsContent(
        files = listOf(
            RecordFile("demo/1_vacunas.pdf", "vacunas.pdf", 238_112),
            RecordFile("demo/2_radiografia.png", "radiografia.png", 1_450_000),
            RecordFile("demo/3_examen.jpg", "examen.jpg", 820_300)
        ),
        loading = false,
        onUploadClick = {},
        onFileClick = {}
    )
}