package com.example.damagotchi_26.ui.evaluacion

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn

// Aviso — primera pérdida, puede seguir
@Composable
fun PerdidaEmbarazoScreen(
    onReintentar: () -> Unit,
    onVolverMenu: () -> Unit
) {
    var alpha by remember { mutableStateOf(0f) }
    val alphaAnimado by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(1500),
        label = "alpha_perdida"
    )

    LaunchedEffect(Unit) { alpha = 1f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A0A2E).copy(alpha = alphaAnimado))
            .zIndex(100f),
        contentAlignment = Alignment.Center
    ) {
        if (alphaAnimado > 0.5f) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1040)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "💔", fontSize = 56.sp)

                    Text(
                        text = "Tu bebe necesita mas cuidados",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = "Varios indicadores han estado muy bajos durante demasiado tiempo. " +
                                "Cuida tu alimentacion, hidratacion e higiene cada dia.\n\n" +
                                "Si vuelve a ocurrir, el embarazo no podra continuar.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onReintentar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleBtn),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Seguir intentandolo",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = onVolverMenu,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text(
                            text = "Volver",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

// Perdida definitiva — segunda vez, va a evaluacion
@Composable
fun PerdidaDefinitivaScreen(
    onVerEvaluacion: () -> Unit
) {
    var alpha by remember { mutableStateOf(0f) }
    val alphaAnimado by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(1500),
        label = "alpha_definitiva"
    )

    LaunchedEffect(Unit) { alpha = 1f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A).copy(alpha = alphaAnimado))
            .zIndex(100f),
        contentAlignment = Alignment.Center
    ) {
        if (alphaAnimado > 0.5f) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0A0A)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "🖤", fontSize = 56.sp)

                    Text(
                        text = "El embarazo no ha podido continuar",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = "A pesar del aviso anterior, los cuidados no fueron suficientes. " +
                                "Es muy importante mantener una buena alimentacion, hidratacion " +
                                "e higiene durante todo el embarazo.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onVerEvaluacion,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5A0A0A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Ver evaluacion final",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}