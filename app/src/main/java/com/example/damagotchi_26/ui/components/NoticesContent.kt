package com.example.damagotchi_26.ui.theme

import com.example.damagotchi_26.data.EventoEspecial
import com.example.damagotchi_26.data.textoParaRol
import com.example.damagotchi_26.domain.Trimestre

fun trimestreDeSemana(semana: Int): Trimestre = when {
    semana in 1..12  -> Trimestre.PRIMERO
    semana in 13..27 -> Trimestre.SEGUNDO
    semana in 28..40 -> Trimestre.TERCERO
    else             -> Trimestre.FINAL
}

data class Recordatorio(val texto: String)

fun mensajesDeInicio(trimestre: Trimestre, rol: String): List<String> = when (rol) {
    "Madre" -> mensajesMadre(trimestre)
    "Padre" -> mensajesPadre(trimestre)
    else    -> mensajesOtro(trimestre)
}

private fun mensajesMadre(trimestre: Trimestre): List<String> = when (trimestre) {
    Trimestre.PRIMERO -> listOf(
        "Tu recorrido comienza aquí. 🌱\n\n" +
                "Estás en la semana 1 de este viaje tan especial. Tu cuerpo ya está " +
                "trabajando, aunque quizás aún no notes nada. Cuídate, descansa y " +
                "escucha cómo te sientes.\n\n" +
                "Cada semana que pase, tu personaje y tú creceréis juntas."
    )
    Trimestre.SEGUNDO -> listOf(
        "¡Bienvenida al segundo trimestre! 🌟\n\n" +
                "Muchas personas encuentran que esta etapa trae más energía y bienestar. " +
                "Aprovecha para realizar actividades que disfrutes y recuerda mantener " +
                "una dieta equilibrada y descansar bien.\n\n" +
                "Esta es una etapa maravillosa. Disfruta de los momentos en los que " +
                "te sientas llena de energía."
    )
    Trimestre.TERCERO -> listOf(
        "¡Ya estás en el tercer trimestre! 🎉\n\n" +
                "El bebé está en la recta final de su crecimiento. Es normal sentirse " +
                "algo más cansada. Recuerda cuidar tu postura, hacer estiramientos " +
                "suaves y descansar siempre que lo necesites.\n\n" +
                "El gran día se acerca. ¡Ya casi llegas!"
    )
    else -> emptyList()
}

private fun mensajesPadre(trimestre: Trimestre): List<String> = when (trimestre) {
    Trimestre.PRIMERO -> listOf(
        "Tu recorrido comienza aquí. 🌱\n\n" +
                "Semana 1. Todo acaba de empezar y tu presencia ya importa. " +
                "Acompañar, escuchar y estar disponible son las mejores cosas " +
                "que puedes hacer ahora.\n\n" +
                "Cada semana que pase, tu personaje y tú creceréis juntos."
    )
    Trimestre.SEGUNDO -> listOf(
        "Ha comenzado el segundo trimestre. 🌟\n\n" +
                "En esta etapa suele haber más energía y estabilidad. Es un buen " +
                "momento para compartir, acompañar y reforzar hábitos saludables " +
                "en equipo.\n\n" +
                "Tu implicación y apoyo siguen siendo fundamentales en esta aventura."
    )
    Trimestre.TERCERO -> listOf(
        "Ha comenzado el tercer trimestre. 🎉\n\n" +
                "Es una etapa importante en la que el descanso, la organización y " +
                "el acompañamiento se vuelven todavía más relevantes.\n\n" +
                "Estás formando parte de un momento muy especial. Tu apoyo puede " +
                "aportar calma, seguridad y bienestar."
    )
    else -> emptyList()
}

private fun mensajesOtro(trimestre: Trimestre): List<String> = when (trimestre) {
    Trimestre.PRIMERO -> listOf(
        "Tu recorrido comienza aquí. 🌱\n\n" +
                "Semana 1. El embarazo acaba de comenzar y cada gesto de apoyo " +
                "cuenta. Acompañar con empatía y cercanía desde el principio " +
                "marca la diferencia.\n\n" +
                "Cada semana que pase, tu personaje y tú creceréis juntos."
    )
    Trimestre.SEGUNDO -> listOf(
        "Ha comenzado el segundo trimestre. 🌟\n\n" +
                "Es un buen momento para seguir acompañando, compartir información " +
                "útil y reforzar rutinas saludables.\n\n" +
                "Cada gesto de apoyo suma en esta etapa."
    )
    Trimestre.TERCERO -> listOf(
        "Ha comenzado el tercer trimestre. 🎉\n\n" +
                "El apoyo, la organización y la atención al bienestar son " +
                "especialmente importantes ahora.\n\n" +
                "Acompañar con calma y cercanía puede marcar una gran diferencia."
    )
    else -> emptyList()
}

val EVENTOS_ESPECIALES: List<EventoEspecial> = listOf(

    EventoEspecial(
        semana     = 7,
        titulo     = "Primera ecografía 🩺",
        textoMadre = "Con 7 semanas ya se puede escuchar tu latido, pequeño. " +
                "Mides unos 10 mm y ya tienes cabeza, tronco y los primeros " +
                "esbozos de brazos y piernas. ¡Todo ha empezado!",
        textoPadre = "Con 7 semanas ya se puede escuchar el latido del bebé. " +
                "Mide unos 10 mm y ya tiene cabeza, tronco y los primeros " +
                "esbozos de brazos y piernas. ¡Todo ha empezado!",
        textoOtro  = "Con 7 semanas ya se puede escuchar el latido del bebé. " +
                "Mide unos 10 mm. Los primeros esbozos de brazos y piernas " +
                "ya están formándose. ¡El viaje ha comenzado!",
        imageRes   = com.example.damagotchi_26.R.drawable.semana7
    ),

    EventoEspecial(
        semana     = 14,
        titulo     = "Ecografía — semana 14 🩺",
        textoMadre = "A las 14 semanas ya estás en el segundo trimestre, pequeño. " +
                "Mides unos 8 cm y pesas alrededor de 25 g. Tu cuerpo crece " +
                "rápido y tus rasgos faciales se van definiendo.",
        textoPadre = "A las 14 semanas el bebé ya está en el segundo trimestre. " +
                "Mide unos 8 cm y pesa alrededor de 25 g. Sus rasgos " +
                "faciales se van definiendo poco a poco.",
        textoOtro  = "A las 14 semanas el bebé ya está en el segundo trimestre. " +
                "Mide unos 8 cm y sus rasgos faciales se van definiendo. " +
                "¡Un gran momento en el desarrollo!",
        imageRes   = com.example.damagotchi_26.R.drawable.semana13
    ),

    EventoEspecial(
        semana     = 22,
        titulo     = "Ecografía morfológica 🩺",
        textoMadre = "La morfológica de la semana 22 es una de las más especiales. " +
                "Ya se puede ver tu carita, contar tus dedos y observar " +
                "cada órgano. Mides unos 27 cm y te mueves con fuerza.",
        textoPadre = "La morfológica de la semana 22 es una de las más especiales. " +
                "Se puede ver el rostro del bebé, contar los dedos y observar " +
                "cada órgano. Ya mide unos 27 cm y se mueve con fuerza.",
        textoOtro  = "La ecografía morfológica de la semana 22 permite ver el " +
                "rostro, los dedos y cada órgano del bebé. Ya mide unos 27 cm.",
        imageRes   = com.example.damagotchi_26.R.drawable.semana20

    ),

    EventoEspecial(
        semana     = 32,
        titulo     = "Ecografía — semana 32 🩺",
        textoMadre = "A las 32 semanas ya estás casi lista, pequeña. Mides unos " +
                "42 cm y pesas cerca de 1,7 kg. Ya tienes los ojos abiertos " +
                "y cierras los puños. El gran día está muy cerca.",
        textoPadre = "A las 32 semanas el bebé está casi listo. Mide unos 42 cm " +
                "y pesa cerca de 1,7 kg. Ya tiene los ojos abiertos y cierra " +
                "los puños. El gran día está muy cerca.",
        textoOtro  = "A las 32 semanas el bebé mide unos 42 cm y pesa cerca de " +
                "1,7 kg. Ya tiene los ojos abiertos. El gran día está " +
                "muy cerca.",
        imageRes   = com.example.damagotchi_26.R.drawable.semana32
    )
)

fun mensajeEventoEspecial(semana: Int, rol: String): String? =
    EVENTOS_ESPECIALES.firstOrNull { it.semana == semana }?.textoParaRol(rol)

val CONSEJOS_GENERALES_MADRE = listOf(
    Recordatorio("Beber agua con frecuencia y priorizar una buena hidratación diaria."),
    Recordatorio("Descansa cuando lo necesites y cuida tu postura al dormir."),
    Recordatorio("Realiza actividad suave si te sienta bien y consulta siempre ante molestias."),
    Recordatorio("Mantén una alimentación equilibrada y variada."),
    Recordatorio("Evita alcohol, tabaco y otras sustancias perjudiciales.")
)
val CONSEJOS_PRIMERO_MADRE = listOf(
    Recordatorio("Si tienes náuseas, beber agua en sorbos pequeños puede ayudarte."),
    Recordatorio("Escucha tu cuerpo y baja el ritmo si te sientes más cansada."),
    Recordatorio("Busca momentos de calma y descanso para adaptarte a esta etapa."),
    Recordatorio("Evita alimentos crudos o poco cocinados."),
    Recordatorio("Comparte tus emociones con personas de confianza.")
)
val CONSEJOS_SEGUNDO_MADRE = listOf(
    Recordatorio("Aprovecha los momentos de mayor energía para cuidarte y moverte."),
    Recordatorio("Mantén una rutina de descanso y postura cómoda al dormir."),
    Recordatorio("Incluye alimentos ricos en hierro y nutrientes importantes."),
    Recordatorio("Continúa con actividad física suave y segura."),
    Recordatorio("Dedica tiempo a conectar contigo y con el proceso.")
)
val CONSEJOS_TERCERO_MADRE = listOf(
    Recordatorio("Usa almohadas para mejorar la postura y descansar mejor."),
    Recordatorio("Prioriza el descanso y evita esfuerzos innecesarios."),
    Recordatorio("Mantén ejercicios suaves si son adecuados para ti."),
    Recordatorio("Recuerda tus revisiones médicas y controles."),
    Recordatorio("Empieza a preparar lo necesario para la llegada del bebé.")
)
val CONSEJOS_GENERALES_PADRE = listOf(
    Recordatorio("Acompañar también es cuidar: escuchar y estar presente ayuda mucho."),
    Recordatorio("Infórmate sobre el embarazo para comprender mejor cada etapa."),
    Recordatorio("Apoya hábitos saludables en casa, como descanso y buena alimentación."),
    Recordatorio("Comparte responsabilidades y ayuda en tareas cotidianas."),
    Recordatorio("Mantén una comunicación abierta y tranquila.")
)
val CONSEJOS_PRIMERO_PADRE = listOf(
    Recordatorio("En el primer trimestre puede haber cansancio y náuseas: ten paciencia y empatía."),
    Recordatorio("Ofrecer ayuda práctica puede marcar una gran diferencia."),
    Recordatorio("Escuchar sin juzgar también es una forma de apoyar."),
    Recordatorio("Ayuda a mantener un ambiente tranquilo y descansado."),
    Recordatorio("Acompaña en la adaptación a esta nueva etapa.")
)
val CONSEJOS_SEGUNDO_PADRE = listOf(
    Recordatorio("Es una buena etapa para participar más activamente en rutinas y cuidados."),
    Recordatorio("Aprovecha para compartir paseos o actividades tranquilas."),
    Recordatorio("Refuerza hábitos saludables en equipo."),
    Recordatorio("Interésate por revisiones, avances y necesidades."),
    Recordatorio("El apoyo emocional sigue siendo muy importante.")
)
val CONSEJOS_TERCERO_PADRE = listOf(
    Recordatorio("En el tercer trimestre, el descanso y la organización cobran más importancia."),
    Recordatorio("Ayuda a preparar lo necesario para la llegada del bebé."),
    Recordatorio("Ofrece apoyo en tareas que puedan resultar más pesadas."),
    Recordatorio("Mantén la calma y transmite seguridad."),
    Recordatorio("Tu presencia puede aportar mucha tranquilidad.")
)
val CONSEJOS_GENERALES_OTRO = listOf(
    Recordatorio("El acompañamiento y la empatía son fundamentales en cualquier etapa."),
    Recordatorio("Escuchar y apoyar en lo cotidiano también ayuda mucho."),
    Recordatorio("Compartir información útil y contrastada puede ser valioso."),
    Recordatorio("Favorecer rutinas saludables y tranquilas suma bienestar."),
    Recordatorio("La cercanía y la comprensión son una forma importante de cuidado.")
)
val CONSEJOS_PRIMERO_OTRO = listOf(
    Recordatorio("El primer trimestre suele traer cambios intensos: acompañar con paciencia ayuda."),
    Recordatorio("Ofrecer apoyo emocional y práctico puede ser muy útil."),
    Recordatorio("Crear un entorno tranquilo favorece el bienestar."),
    Recordatorio("Escuchar y validar emociones es importante."),
    Recordatorio("Cada pequeño gesto de apoyo cuenta.")
)
val CONSEJOS_SEGUNDO_OTRO = listOf(
    Recordatorio("El segundo trimestre suele ser más estable: es buena etapa para reforzar apoyo."),
    Recordatorio("Comparte momentos tranquilos y hábitos saludables."),
    Recordatorio("Ayuda a mantener rutinas equilibradas."),
    Recordatorio("Interesarte por el proceso también acompaña."),
    Recordatorio("El bienestar compartido mejora la experiencia.")
)
val CONSEJOS_TERCERO_OTRO = listOf(
    Recordatorio("En el tercer trimestre, la ayuda práctica y la atención al descanso son muy valiosas."),
    Recordatorio("Es un buen momento para colaborar en la organización."),
    Recordatorio("Apoyar con calma y cercanía aporta seguridad."),
    Recordatorio("La constancia en el acompañamiento es importante."),
    Recordatorio("Prepararse juntos ayuda a vivir esta etapa con más tranquilidad.")
)

fun recordatoriosPorTrimestre(trimestre: Trimestre, rol: String): List<Recordatorio> =
    when (rol) {
        "Madre" -> when (trimestre) {
            Trimestre.PRIMERO -> CONSEJOS_GENERALES_MADRE + CONSEJOS_PRIMERO_MADRE
            Trimestre.SEGUNDO -> CONSEJOS_GENERALES_MADRE + CONSEJOS_SEGUNDO_MADRE
            Trimestre.TERCERO -> CONSEJOS_GENERALES_MADRE + CONSEJOS_TERCERO_MADRE
            else              -> CONSEJOS_GENERALES_MADRE
        }
        "Padre" -> when (trimestre) {
            Trimestre.PRIMERO -> CONSEJOS_GENERALES_PADRE + CONSEJOS_PRIMERO_PADRE
            Trimestre.SEGUNDO -> CONSEJOS_GENERALES_PADRE + CONSEJOS_SEGUNDO_PADRE
            Trimestre.TERCERO -> CONSEJOS_GENERALES_PADRE + CONSEJOS_TERCERO_PADRE
            else              -> CONSEJOS_GENERALES_PADRE
        }
        else -> when (trimestre) {
            Trimestre.PRIMERO -> CONSEJOS_GENERALES_OTRO + CONSEJOS_PRIMERO_OTRO
            Trimestre.SEGUNDO -> CONSEJOS_GENERALES_OTRO + CONSEJOS_SEGUNDO_OTRO
            Trimestre.TERCERO -> CONSEJOS_GENERALES_OTRO + CONSEJOS_TERCERO_OTRO
            else              -> CONSEJOS_GENERALES_OTRO
        }
    }

fun consejoDelDia(trimestre: Trimestre, rol: String, dia: Int): String? {
    val lista = recordatoriosPorTrimestre(trimestre, rol)
    if (lista.isEmpty()) return null
    return lista[dia % lista.size].texto
}