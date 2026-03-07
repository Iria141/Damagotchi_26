package com.example.damagotchi_26.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.ui.Color.Color.BorderGray
import com.example.damagotchi_26.ui.Color.Color.CardGray


@Composable
fun AuthBackground(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp)
            .background(com.example.damagotchi_26.ui.Color.Color.PinkBg),
        content = content
    )
}

@Composable
fun AuthCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(com.example.damagotchi_26.ui.Color.Color.PinkBg, RoundedCornerShape(30.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
fun PrimaryAuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = com.example.damagotchi_26.ui.Color.Color.PurpleBtn),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(44.dp)
    ) {
        Text(text, color = Color.White)
    }
}


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    trailingIcon: (@Composable (() -> Unit))? = null, //añadir icono al calendario
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,

            label = {
                Text(
                    text = label,
                    fontSize = 18.sp
                )
            },
            singleLine = true, //solo una linea, no permite salto de linea

            visualTransformation =
                if (isPassword) PasswordVisualTransformation()
                else VisualTransformation.None,

            isError = isError,
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),


            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardGray,
                unfocusedContainerColor = CardGray,
                disabledContainerColor = CardGray,

                focusedBorderColor = BorderGray,
                unfocusedBorderColor = BorderGray,
                disabledBorderColor = BorderGray,

                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray,

                cursorColor = Color.DarkGray
            )
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun AuthInfoBox(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CardGray, RoundedCornerShape(4.dp))
            .border(1.dp, BorderGray, RoundedCornerShape(4.dp))
            .padding(18.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
