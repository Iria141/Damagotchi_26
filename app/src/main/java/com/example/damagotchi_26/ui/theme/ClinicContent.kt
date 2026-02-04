package com.example.damagotchi_26.ui.theme
import com.example.damagotchi_26.domain.Trimestre

fun trimestreDeSemana(semana: Int): Trimestre = when {
    semana in 1..13 -> Trimestre.PRIMERO
    semana in 14..27 -> Trimestre.SEGUNDO
    semana in 28..40 -> Trimestre.TERCERO
    else -> Trimestre.FINAL
}

data class MensajeEtapa(
    val trimestre: Trimestre,
    val textos: List<String>
)

val MENSAJES_TRIMESTRE = listOf(
    MensajeEtapa(
        trimestre = Trimestre.PRIMERO,
        textos = listOf(
            "¡Felicidades! Estás en el primer trimestre de tu embarazo. Es un momento lleno de emociones. Tu cuerpo se está " +
                    "adaptando y podrías experimentar algunos cambios como cansancio o náuseas. Recuerda cuidar de ti, descansar " +
                    "cuando lo necesites y mantenerte hidratada. Este es solo el comienzo de un viaje especial. \n \n Tómate tu " +
                    "tiempo, cada pequeño paso es importante. Estás haciendo un gran trabajo cuidando de ti y de tu bebé."
        )
    ),
    MensajeEtapa(
        trimestre = Trimestre.SEGUNDO,
        textos = listOf(
            "Bienvenida al segundo trimestre. Muchas personas encuentran que esta etapa trae un aumento de energía y bienestar. " +
                    "Aprovecha para realizar actividades que disfrutes y recuerda mantener una dieta equilibrada y descansar bien. " +
                    "Tu bebé está creciendo rápidamente y tú también sentirás esos cambios. \n\n  Esta es una etapa maravillosa. " +
                    "Disfruta de los momentos en los que te sientas llena de energía y recuerda que te estás preparando para lo mejor."

        )
    ),
    MensajeEtapa(
        trimestre = Trimestre.TERCERO,
        textos = listOf(
            "¡Ya estás en el tercer trimestre! El bebé está en la recta final de su crecimiento. Es normal que te sientas algo más " +
                    "cansada y con ganas de descansar. Recuerda cuidar de tu postura, realizar estiramientos suaves y descansar " +
                    "siempre que lo necesites. El gran día se acerca, y estás cada vez más preparada.\n\n El esfuerzo y el amor " +
                    "que has puesto en estos meses te han traído hasta aquí. Estás más fuerte y preparada de lo que imaginas. ¡Ya " +
                    "casi llegas!"

        )
    )
)

fun mensajesDeInicio(trimestre: com.example.damagotchi_26.domain.Trimestre): List<String> {
    return MENSAJES_TRIMESTRE.firstOrNull { it.trimestre == trimestre }?.textos ?: emptyList()
}




data class Recordatorio(
    val texto: String
)

val CONSEJOS_GENERALES = listOf(
    Recordatorio( "Beber 8 vasos de agua, llevar una botella, preferir agua sobre bebidas azucaradas."),
    Recordatorio( "Descansa cuando lo necesites, usa almohadas para postura y evita pantallas antes de dormir."),
    Recordatorio( "Ejercicios suaves y salir al aire libre son beneficiosos."),
    Recordatorio( "Higiene bucal, hidratar la piel y usar cremas si lo necesitas."),
    Recordatorio( "Evita alcohol, tabaco y drogas; consulta al médico ante síntomas inesperados.")
)

val CONSEJOS_PRIMERO = listOf(
    Recordatorio("Si tienes náuseas, beber agua en sorbos pequeños puede ayudarte."),
    Recordatorio("Respiración profunda y rutina de sueño para reducir el cansancio inicial."),
    Recordatorio("Ejercicios de visualización para conectar con el embarazo."),
    Recordatorio("Evita alimentos crudos o poco cocidos."),
    Recordatorio("Busca calma, respiración y comparte pensamientos con personas de confianza.")
)

val CONSEJOS_SEGUNDO = listOf(
    Recordatorio("Rutina de sueño y almohadas para postura."),
    Recordatorio("Caminatas al aire libre y bajo impacto."),
    Recordatorio("Incluye alimentos ricos en hierro (espinacas, carne magra)."),
    Recordatorio("Buena higiene bucal; puede aumentar sensibilidad dental."),
    Recordatorio ("Compartir emociones y momentos de relajación.")
)

val CONSEJOS_TERCERO = listOf(
    Recordatorio("Almohadas entre las piernas y respiración para molestias físicas."),
    Recordatorio( "Ejercicios suaves para suelo pélvico."),
    Recordatorio("Snacks nutritivos como frutos secos para energía."),
    Recordatorio( "Hidrata la piel para minimizar molestias y estrías."),
    Recordatorio( "Recuerda visitas médicas y exámenes de rutina."),
    Recordatorio( "Últimos pasos: prepara la mochila del hospital y un espacio para tu bebé.")
)

fun recordatoriosPorTrimestre(trimestre: com.example.damagotchi_26.domain.Trimestre): List<Recordatorio> = when (trimestre) {
    Trimestre.PRIMERO -> CONSEJOS_GENERALES + CONSEJOS_PRIMERO
    Trimestre.SEGUNDO -> CONSEJOS_GENERALES + CONSEJOS_SEGUNDO
    Trimestre.TERCERO -> CONSEJOS_GENERALES + CONSEJOS_TERCERO
    else -> CONSEJOS_GENERALES
}