package com.example.damagotchi_26.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@Composable
fun SimpleSuccess(
    message: String,
    buttonText: String,
    onButton: () -> Unit
) {
    AuthBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AuthCard(modifier = Modifier.fillMaxWidth(0.75f)) {
                    Text(message, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(Modifier.height(16.dp))

            PrimaryAuthButton(
                text = buttonText,
                onClick = onButton,
                modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally)
            )
        }
    }
}
