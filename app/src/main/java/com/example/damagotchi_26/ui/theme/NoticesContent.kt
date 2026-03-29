package com.example.damagotchi_26.ui.theme

import com.example.damagotchi_26.domain.Trimestre

fun trimestreDeSemana(semana: Int): Trimestre = when {
    semana in 1..13 -> Trimestre.PRIMERO
    semana in 14..27 -> Trimestre.SEGUNDO
    semana in 28..40 -> Trimestre.TERCERO
    else -> Trimestre.FINAL
}

data class Recordatorio(
    val texto: String
)

fun mensajesDeInicio(trimestre: Trimestre, rol: String): List<String> {
    return when (rol) {
        "Madre" -> mensajesMadre(trimestre)
        "Padre" -> mensajesPadre(trimestre)
        else -> mensajesOtro(trimestre)
    }
}

private fun mensajesMadre(trimestre: Trimestre): List<String> = when (trimestre) {
    Trimestre.PRIMERO -> listOf(
        "¡Felicidades! Estás en el primer trimestre de tu embarazo. Es un momento lleno de emociones. Tu cuerpo se está adaptando y podrías experimentar cambios como cansancio o náuseas. Recuerda cuidar de ti, descansar cuando lo necesites y mantenerte hidratada.\n\nTómate tu tiempo, cada pequeño paso es importante. Estás haciendo un gran trabajo cuidando de ti y de tu bebé."
    )

    Trimestre.SEGUNDO -> listOf(
        "Bienvenida al segundo trimestre. Muchas personas encuentran que esta etapa trae un aumento de energía y bienestar. Aprovecha para realizar actividades que disfrutes y recuerda mantener una dieta equilibrada y descansar bien.\n\nEsta es una etapa maravillosa. Disfruta de los momentos en los que te sientas llena de energía y recuerda que te estás preparando para lo mejor."
    )

    Trimestre.TERCERO -> listOf(
        "¡Ya estás en el tercer trimestre! El bebé está en la recta final de su crecimiento. Es normal que te sientas algo más cansada y con ganas de descansar. Recuerda cuidar de tu postura, realizar estiramientos suaves y descansar siempre que lo necesites.\n\nEl gran día se acerca, y estás cada vez más preparada. ¡Ya casi llegas!"
    )

    else -> emptyList()
}

private fun mensajesPadre(trimestre: Trimestre): List<String> = when (trimestre) {
    Trimestre.PRIMERO -> listOf(
        "Ha comenzado el primer trimestre. Es una etapa de muchos cambios y adaptación. Tu apoyo, paciencia y compañía pueden marcar una gran diferencia desde el inicio.\n\nAcompañar también es cuidar. Estar presente, escuchar y comprender ya es una ayuda muy valiosa."
    )

    Trimestre.SEGUNDO -> listOf(
        "Ha comenzado el segundo trimestre. En esta etapa suele haber más energía y estabilidad. Es un buen momento para compartir, acompañar y reforzar hábitos saludables en equipo.\n\nTu implicación y apoyo siguen siendo fundamentales en esta aventura."
    )

    Trimestre.TERCERO -> listOf(
        "Ha comenzado el tercer trimestre. Es una etapa importante en la que el descanso, la organización y el acompañamiento se vuelven todavía más relevantes.\n\nEstás formando parte de un momento muy especial. Tu apoyo puede aportar calma, seguridad y bienestar."
    )

    else -> emptyList()
}

private fun mensajesOtro(trimestre: Trimestre): List<String> = when (trimestre) {
    Trimestre.PRIMERO -> listOf(
        "Ha comenzado el primer trimestre. Es una etapa de inicio, adaptación y muchos cambios. El acompañamiento cercano, la empatía y la atención pueden ayudar mucho.\n\nEstar presente y apoyar en este proceso también es una forma de cuidar."
    )

    Trimestre.SEGUNDO -> listOf(
        "Ha comenzado el segundo trimestre. Es un buen momento para seguir acompañando, compartir información útil y reforzar rutinas saludables.\n\nCada gesto de apoyo suma en esta etapa."
    )

    Trimestre.TERCERO -> listOf(
        "Ha comenzado el tercer trimestre. En esta etapa, el apoyo, la organización y la atención al bienestar son especialmente importantes.\n\nAcompañar con calma y cercanía puede marcar una gran diferencia."
    )

    else -> emptyList()
}

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

fun recordatoriosPorTrimestre(trimestre: Trimestre, rol: String): List<Recordatorio> {
    return when (rol) {
        "Madre" -> when (trimestre) {
            Trimestre.PRIMERO -> CONSEJOS_GENERALES_MADRE + CONSEJOS_PRIMERO_MADRE
            Trimestre.SEGUNDO -> CONSEJOS_GENERALES_MADRE + CONSEJOS_SEGUNDO_MADRE
            Trimestre.TERCERO -> CONSEJOS_GENERALES_MADRE + CONSEJOS_TERCERO_MADRE
            else -> CONSEJOS_GENERALES_MADRE
        }

        "Padre" -> when (trimestre) {
            Trimestre.PRIMERO -> CONSEJOS_GENERALES_PADRE + CONSEJOS_PRIMERO_PADRE
            Trimestre.SEGUNDO -> CONSEJOS_GENERALES_PADRE + CONSEJOS_SEGUNDO_PADRE
            Trimestre.TERCERO -> CONSEJOS_GENERALES_PADRE + CONSEJOS_TERCERO_PADRE
            else -> CONSEJOS_GENERALES_PADRE
        }

        else -> when (trimestre) {
            Trimestre.PRIMERO -> CONSEJOS_GENERALES_OTRO + CONSEJOS_PRIMERO_OTRO
            Trimestre.SEGUNDO -> CONSEJOS_GENERALES_OTRO + CONSEJOS_SEGUNDO_OTRO
            Trimestre.TERCERO -> CONSEJOS_GENERALES_OTRO + CONSEJOS_TERCERO_OTRO
            else -> CONSEJOS_GENERALES_OTRO
        }
    }
}



data class EventoEspecial(
    val semana: Int,
    val textoMadre: String,
    val textoPadre: String,
    val textoOtro: String
)

val EVENTOS_ESPECIALES = listOf(
    EventoEspecial(
        semana = 12,
        textoMadre = "¡Semana 12! Tu embarazo sigue avanzando y cada paso cuenta. Es un momento importante para seguir cuidándote.",
        textoPadre = "¡Semana 12! Tu acompañamiento en esta etapa sigue siendo muy valioso.",
        textoOtro = "¡Semana 12! El apoyo y la cercanía siguen siendo importantes en este proceso."
    ),
    EventoEspecial(
        semana = 20,
        textoMadre = "¡Semana 20! Estás en una etapa muy especial del embarazo. Sigue escuchando tu cuerpo y cuidando tu bienestar.",
        textoPadre = "¡Semana 20! Es un gran momento para seguir acompañando y compartiendo esta experiencia.",
        textoOtro = "¡Semana 20! El acompañamiento y la atención siguen siendo fundamentales."
    ),
    EventoEspecial(
        semana = 36,
        textoMadre = "¡Semana 36! Ya queda muy poquito. Descansa, cuida tu energía y prepárate con calma.",
        textoPadre = "¡Semana 36! Se acerca un momento importante. Tu apoyo y tranquilidad serán muy valiosos.",
        textoOtro = "¡Semana 36! Es una etapa clave para acompañar con calma, organización y cercanía."
    )
)

fun mensajeEventoEspecial(semana: Int, rol: String): String? {
    val evento = EVENTOS_ESPECIALES.firstOrNull { it.semana == semana } ?: return null

    return when (rol) {
        "Madre" -> evento.textoMadre
        "Padre" -> evento.textoPadre
        else -> evento.textoOtro
    }
}

fun consejoDelDia(trimestre: Trimestre, rol: String, dia: Int): String? {
    val lista = recordatoriosPorTrimestre(trimestre, rol)
    if (lista.isEmpty()) return null

    val indice = dia % lista.size
    return lista[indice].texto
}