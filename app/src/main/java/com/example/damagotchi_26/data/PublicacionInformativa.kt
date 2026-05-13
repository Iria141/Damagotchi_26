package com.example.damagotchi_26.data

data class PublicacionInformativa(
    val id: String = "",
    val titulo: String = "",
    val contenido: String = "",
    val categoria: String = "",
    val semanaInicio: Int = 1,
    val semanaFin: Int = 40,
    val rolDestino: List<String> = emptyList(),
    val destacada: Boolean = false,
    val fuente: String = "",
    val fuenteURL: String = "",
    val fechaCreacion: Long = System.currentTimeMillis(),
    val creadaAdmin: Boolean = true,
    val imagenUrl: String = ""
)

object CategoriaInformativa {
    const val HIDRATACION = "hidratacion"
    const val MASCOTAS = "mascotas"
    const val ALIMENTACION = "alimentacion"
    const val SALUD = "salud"
    const val RECURSOS = "recursos"
    const val DESCANSO = "descanso"
    const val BIENESTAR_EMOCIONAL = "bienestar_emocional"
    const val DESARROLLO_BEBE = "desarrollo_bebe"
}

object RolUsuario {
    const val MAMA = "mamá"
    const val ACOMPANANTE = "acompañante"
    const val OTRO = "otro"
}