package com.example.damagotchi_26.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.damagotchi_26.ui.Color.Color.BgHeader1
import com.example.damagotchi_26.ui.Color.Color.BgHeader2
import com.example.damagotchi_26.ui.Color.Color.BgSurface
import com.example.damagotchi_26.ui.Color.Color.DividerClr
import com.example.damagotchi_26.ui.Color.Color.TextPrimary

private fun emojiParaSemana(semana: Int): String = when (semana) {
    1    -> "🌱"
    13   -> "🌟"
    28   -> "🎉"
    else -> "💫"
}

private fun subtituloParaSemana(semana: Int): String = when (semana) {
    1    -> "Semana 1 · Primer trimestre"
    13   -> "Semana 13 · Segundo trimestre"
    28   -> "Semana 28 · Tercer trimestre"
    else -> "Semana $semana"
}

@Composable
fun TrimestreDialog(
    titulo: String,
    mensaje: String,
    semana: Int,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn(tween(350)) + scaleIn(tween(350), initialScale = 0.88f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .background(BgSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(BgHeader1, BgHeader2)))
                            .padding(top = 24.dp, bottom = 20.dp, start = 24.dp, end = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text     = emojiParaSemana(semana),
                                    fontSize = 26.sp
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text       = titulo,
                                color      = Color.White,
                                fontSize   = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign  = TextAlign.Center,
                                lineHeight = 24.sp
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text      = subtituloParaSemana(semana),
                                color     = Color.White.copy(alpha = 0.75f),
                                fontSize  = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text       = mensaje,
                        color      = TextPrimary,
                        fontSize   = 14.sp,
                        lineHeight = 22.sp,
                        textAlign  = TextAlign.Start,
                        modifier   = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(1.dp)
                            .background(DividerClr)
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick  = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(48.dp),
                        shape  = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgHeader1,
                            contentColor   = Color.White
                        )
                    ) {
                        Text(
                            text       = if (semana == 1) "¡Empezar!" else "Continuar",
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 16.sp
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}