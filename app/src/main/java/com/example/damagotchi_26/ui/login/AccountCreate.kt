package com.example.damagotchi_26.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthInfoBox

@Composable
fun AccountCreated(
    onGoLogin: () -> Unit
) {
    AuthBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthInfoBox(
                    text = "Su cuenta ha sido creada con éxito."
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = onGoLogin,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Inicio")
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountCreatedScreenPreview() {
    MaterialTheme {
        Surface {
            AccountCreated(
                onGoLogin = { }
            )
        }
    }
}