package com.example.damagotchi_26.ui.rooms

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
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn

enum class NotaFinal { EXCELENTE, BIEN, MEJORABLE, DEFICIENTE, PERDIDA }

fun calcularNota(pet: Pet, perdidaDefinitiva: Boolean): NotaFinal {
    if (perdidaDefinitiva) return NotaFinal.PERDIDA

    if (pet.diasEvaluados == 0) return NotaFinal.MEJORABLE

    val promedioEnergia = pet.sumaEnergia / pet.diasEvaluados
    val promedioHambre = pet.sumaHambre / pet.diasEvaluados
    val promedioSed = pet.sumaSed / pet.diasEvaluados
    val promedioLimpieza = pet.sumaLimpieza / pet.diasEvaluados
    val promedioActividad = pet.sumaActividad / pet.diasEvaluados
    val promedioDescanso = pet.sumaDescanso / pet.diasEvaluados

    val promedioGeneral = (promedioEnergia + promedioHambre + promedioSed +
            promedioLimpieza + promedioActividad + promedioDescanso) / 6

    return when {
        promedioGeneral >= 75 -> NotaFinal.EXCELENTE
        promedioGeneral >= 50 -> NotaFinal.BIEN
        promedioGeneral >= 30 -> NotaFinal.MEJORABLE
        else -> NotaFinal.DEFICIENTE
    }
}

@Composable
fun EvaluacionFinalScreen(
    pet: Pet,
    perdidaDefinitiva: Boolean = false,
    onVolverMenu: () -> Unit
) {
    val nota = calcularNota(pet, perdidaDefinitiva)

    val (emoji, titulo, mensaje, colorFondo, colorCard) = when (nota) {
        NotaFinal.EXCELENTE -> listOf(
            "🌟",
            "¡Excelente!",
            "¡Felicitaciones! Has cuidado excelentemente a tu personaje durante todo el embarazo.",
            Color(0xFF1A0A2E),
            Color(0xFF2A1060)
        )
        NotaFinal.BIEN -> listOf(
            "😊",
            "¡Bien!",
            "Tu cuidado ha sido bueno, pero podrías mejorar algunos aspectos para el bienestar total del personaje.",
            Color(0xFF0A1A2E),
            Color(0xFF103060)
        )
        NotaFinal.MEJORABLE -> listOf(
            "😐",
            "Mejorable",
            "El personaje ha recibido pocos cuidados, trata de estar más atento la próxima vez.",
            Color(0xFF1A1A0A),
            Color(0xFF3A3A10)
        )
        NotaFinal.DEFICIENTE -> listOf(
            "😞",
            "Deficiente",
            "El personaje ha recibido un cuidado pésimo y ha sufrido debido a la falta de atención. ¡Intenta ser más cuidadoso en el futuro!",
            Color(0xFF1A0A0A),
            Color(0xFF3A1010)
        )
        NotaFinal.PERDIDA -> listOf(
            "🖤",
            "Pérdida del embarazo",
            "Lamentablemente, debido a los bajos niveles de cuidado, el personaje ha perdido el embarazo. Te animamos a estar más atento a los medidores de salud para brindar un mejor apoyo en el futuro.",
            Color(0xFF0A0A0A),
            Color(0xFF1A0A0A)
        )
    }

    var alpha by remember { mutableStateOf(0f) }
    val alphaAnimado by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(1500),
        label = "alpha_evaluacion"
    )

    LaunchedEffect(Unit) { alpha = 1f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background((colorFondo as Color).copy(alpha = alphaAnimado))
            .zIndex(100f),
        contentAlignment = Alignment.Center
    ) {
        if (alphaAnimado > 0.5f) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colorCard as Color),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = emoji as String, fontSize = 60.sp)

                    Text(
                        text = titulo as String,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    // Estadísticas
                    if (pet.diasEvaluados > 0) {
                        val promedioGeneral = (
                                pet.sumaEnergia + pet.sumaHambre + pet.sumaSed +
                                        pet.sumaLimpieza + pet.sumaActividad + pet.sumaDescanso
                                ) / (pet.diasEvaluados * 6)

                        Text(
                            text = "Promedio general: $promedioGeneral%",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.SemiBold
                        )

                        LinearProgressIndicator(
                            progress = { promedioGeneral / 100f },
                            modifier = Modifier.fillMaxWidth().height(10.dp),
                            color = when (nota) {
                                NotaFinal.EXCELENTE -> Color(0xFF66BB6A)
                                NotaFinal.BIEN -> Color(0xFF42A5F5)
                                NotaFinal.MEJORABLE -> Color(0xFFFFB300)
                                else -> Color(0xFFEF5350)
                            },
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )
                    }

                    Text(
                        text = mensaje as String,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onVolverMenu,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleBtn),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Volver al menú",
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