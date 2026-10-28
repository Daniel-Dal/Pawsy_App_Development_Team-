package com.example.pawsy.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import androidx.compose.ui.unit.sp
@Composable
fun FoodRecognition(
    navController: NavController,
    vm: FoodRecognitionViewModel = viewModel()
) {
    val context = LocalContext.current


    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { vm.analizarUri(context.contentResolver, it) }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        bmp?.let { vm.analizarBitmap(it) }
    }

    PawsyTheme {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.pawsyColors.editTextAgregarMascota)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.pawsyColors.fondoPerfil)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.reconocimiento_visual),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.pawsyColors.blanco
                    )
                    Image(
                        painter = painterResource(id = R.drawable.pawsy_paw_white),
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(105f)
                            .offset(y = (-10).dp, x = (-10).dp)
                            .size(80.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {


                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(1f)
                            .border(2.dp, Color.Black)
                            .background(Color.White.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val bmp = vm.bitmap
                        if (bmp != null) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text("🍽️", style = MaterialTheme.typography.displayMedium)
                        }
                    }




                    val scrollState = rememberScrollState()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.pawsyColors.editTextAgregarMascota)
                            .drawWithContent {
                                drawContent()
                                if (scrollState.maxValue > 0) {
                                    val viewH = size.height
                                    val totalH = viewH + scrollState.maxValue
                                    val barH = viewH * viewH / totalH
                                    val barY = scrollState.value * (viewH - barH) / scrollState.maxValue
                                    drawRoundRect(
                                        color = Color.White,
                                        topLeft = Offset(size.width - 12.dp.toPx(), barY),
                                        size = Size(5.dp.toPx(), barH),
                                        cornerRadius = CornerRadius(3.dp.toPx())
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (val s = vm.state) {
                            is RecognitionState.Loading -> CircularProgressIndicator(color = Color.White)

                            is RecognitionState.Idle -> Text(
                                text = "Toma una foto o elige una de tu galería y Pawsy te dirá qué comida es 🐾",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )

                            is RecognitionState.Success -> Text(
                                text = s.text,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
                            )

                            is RecognitionState.Error -> Text(
                                text = s.message,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }


                    val loading = vm.state is RecognitionState.Loading
                    Row(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Button(
                            onClick = { cameraLauncher.launch(null) },
                            enabled = !loading,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
                                contentColor = Color.White
                            )
                        ) {
                            Text("📷 Cámara", fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            enabled = !loading,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
                                contentColor = Color.White
                            )
                        ) { Text("🖼️ Galería", fontSize = 14.sp) }
                    }
                }
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.pawsyColors.editTextAgregarMascota),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_paw_menu),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    showSystemUi = true
)
@Composable
fun FoodRecognitionPreview() {
    val navController = rememberNavController()
    FoodRecognition(navController = navController)
}