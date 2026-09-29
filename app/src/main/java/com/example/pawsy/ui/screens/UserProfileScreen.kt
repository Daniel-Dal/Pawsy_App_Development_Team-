package com.example.pawsy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.pawsy.R
import com.example.pawsy.ui.components.PawsyButton
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors

@Composable
fun MyProfileText() {
    PawsyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.fondoPerfil)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(id = R.string.título_pantalla6),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                )

                Image(
                    painter = painterResource(id = R.drawable.pawsy_paw),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(105f)
                        .offset(y = (-150).dp, x = (-40).dp)
                        .size(80.dp)
                )
            }

            Text( text = stringResource(id = R.string.boton_datosUsuario),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.textEditTextAgregarMascota)

            PawsyButton(

                onClick = { /* Nothing yet */ }
            )

            Text( text = stringResource(id = R.string.boton_idioma),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.textEditTextAgregarMascota)

            PawsyButton(

                onClick = { /* Nothing yet */ }
            )

            Text( text = stringResource(id = R.string.boton_notificacion),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.textEditTextAgregarMascota)

            PawsyButton(


                onClick = { /* Nothing yet */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(8.dp)
            )
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
fun MyProfileTextPreview() {
    MyProfileText()
}