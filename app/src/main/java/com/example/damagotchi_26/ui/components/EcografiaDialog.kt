package com.example.damagotchi_26.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.damagotchi_26.data.EventoEspecial
import com.example.damagotchi_26.data.textoParaRol
import com.example.damagotchi_26.ui.Color.Color.BgHeader1
import com.example.damagotchi_26.ui.Color.Color.BgHeader2
import com.example.damagotchi_26.ui.Color.Color.BgImageBox
import com.example.damagotchi_26.ui.Color.Color.BgSurface
import com.example.damagotchi_26.ui.Color.Color.Divider
import com.example.damagotchi_26.ui.Color.Color.TextPrimary

@Composable
fun EcografiaDialog(
    evento: EventoEspecial,
    rol: String,
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
                            .padding(vertical = 18.dp, horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = evento.titulo,
                            color      = Color.White,
                            fontSize   = 19.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign  = TextAlign.Center,
                            lineHeight = 26.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (evento.imageRes != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(230.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(BgImageBox)
                        ) {
                            Image(
                                painter            = painterResource(id = evento.imageRes),
                                contentDescription = evento.titulo,
                                contentScale       = ContentScale.Crop,
                                modifier           = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(listOf(Color.Transparent, BgSurface))
                                    )
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                    }

                    Text(
                        text       = evento.textoParaRol(rol),
                        color      = TextPrimary,
                        fontSize   = 14.sp,
                        lineHeight = 22.sp,
                        textAlign  = TextAlign.Start,
                        modifier   = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )

                    Spacer(Modifier.height(22.dp))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(1.dp)
                            .background(Divider)
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
                            text       = "Continuar",
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