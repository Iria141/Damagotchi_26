package com.example.damagotchi_26.repository

import com.google.firebase.firestore.FirebaseFirestore

object SeedCommunity {

    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("community_posts")

    private data class PostSeed(
        val titulo: String,
        val contenido: String,
        val tipo: String,
        val authorName: String,
        val authorRole: String,
        val isPinned: Boolean = false,
        val createdAt: Long
    )

    private val posts = listOf(

        // ── Bienvenida ────────────────────────────────────────────────────────
        PostSeed(
            titulo      = "¡Bienvenida a la comunidad Damagotchi! 🌸",
            contenido   = "Este es tu espacio para compartir, preguntar y acompañarte durante el embarazo. " +
                    "Aquí puedes contar cómo te va, resolver dudas o simplemente saber que no estás sola. " +
                    "¡Nos alegra que estés aquí!",
            tipo        = "anuncio",
            authorName  = "Equipo Damagotchi",
            authorRole  = "admin",
            isPinned    = true,
            createdAt   = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
        ),

        PostSeed(
            titulo      = "Normas de la comunidad 💜",
            contenido   = "Para que este espacio sea seguro y agradable para todas:\n\n" +
                    "· Respeta siempre a los demás, independientemente de su situación.\n" +
                    "· Comparte desde la experiencia propia, sin juzgar.\n" +
                    "· Ante dudas médicas, consulta siempre con tu profesional de salud.\n" +
                    "· Estamos aquí para acompañar, no para dar diagnósticos.\n\n" +
                    "¡Gracias por cuidar este espacio entre todas!",
            tipo        = "anuncio",
            authorName  = "Equipo Damagotchi",
            authorRole  = "admin",
            isPinned    = true,
            createdAt   = System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L
        ),

        // ── Consejos de embarazo ──────────────────────────────────────────────
        PostSeed(
            titulo      = "Consejo para las náuseas del primer trimestre 🍋",
            contenido   = "A mí me ayudó mucho tener siempre algo ligero a mano para comer nada más levantarme, " +
                    "antes incluso de salir de la cama. Unas galletas o un par de nueces. " +
                    "También el jengibre en infusión, aunque no a todo el mundo le sienta bien. " +
                    "¿Qué os ha funcionado a vosotras?",
            tipo        = "consejo",
            authorName  = "Laura",
            authorRole  = "Madre",
            createdAt   = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000L
        ),

        PostSeed(
            titulo      = "Dormir en el tercer trimestre: lo que nadie te cuenta 😅",
            contenido   = "Con la barriga ya grande, la almohada de embarazo fue un cambio radical para mí. " +
                    "Dormir de lado izquierdo mejora la circulación, pero al principio cuesta acostumbrarse. " +
                    "También ayuda elevar un poco las piernas si tienes retención. " +
                    "¿Alguien más tiene trucos para descansar mejor en esta etapa?",
            tipo        = "consejo",
            authorName  = "María",
            authorRole  = "Madre",
            createdAt   = System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000L
        ),

        // ── Experiencias personales ───────────────────────────────────────────
        PostSeed(
            titulo      = "Primera ecografía: un momento que no olvidaré 🩺",
            contenido   = "Hoy hemos ido a la primera ecografía y ver ese pequeño corazón latiendo " +
                    "ha sido algo difícil de describir. Todo se vuelve muy real de repente. " +
                    "Estoy en la semana 8 y aunque estoy cansada y con náuseas, " +
                    "ahora todo tiene otro sentido. ¿Cómo fue vuestra primera vez?",
            tipo        = "experiencia",
            authorName  = "Sara",
            authorRole  = "Madre",
            createdAt   = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L
        ),

        PostSeed(
            titulo      = "Mi experiencia acompañando el embarazo desde el otro lado 💙",
            contenido   = "Soy el padre y quiero compartir que este proceso también es intenso para nosotros, " +
                    "aunque de otra manera. A veces no sabes muy bien cómo ayudar " +
                    "o tienes miedo de hacer algo mal. Lo que más me ha servido es preguntar, " +
                    "estar presente y no asumir que sé lo que necesita. " +
                    "¿Hay más papás por aquí?",
            tipo        = "experiencia",
            authorName  = "Carlos",
            authorRole  = "Padre",
            createdAt   = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L
        ),

        // ── Preguntas frecuentes ──────────────────────────────────────────────
        PostSeed(
            titulo      = "¿Es normal sentir tanto cansancio en el primer trimestre?",
            contenido   = "Llevo tres semanas que me quedo dormida en el sofá a las ocho de la tarde " +
                    "y por las mañanas me cuesta horrores levantarme. " +
                    "Mi médica me dijo que es completamente normal, que el cuerpo está trabajando " +
                    "muchísimo aunque no se vea. Pero me gustaría saber si a vosotras " +
                    "también os pasó así de fuerte.",
            tipo        = "pregunta",
            authorName  = "Ana",
            authorRole  = "Madre",
            createdAt   = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000L
        ),

        PostSeed(
            titulo      = "¿Cómo explicarle el embarazo a un niño pequeño?",
            contenido   = "Tenemos un hijo de 3 años y no sabemos muy bien cómo contarle " +
                    "que va a tener un hermanito. ¿A qué edad lo entendéis vosotros? " +
                    "¿Con cuentos, con explicaciones sencillas? " +
                    "Cualquier idea o experiencia es bienvenida.",
            tipo        = "pregunta",
            authorName  = "Pedro",
            authorRole  = "Padre",
            createdAt   = System.currentTimeMillis() - 12 * 60 * 60 * 1000L
        )
    )

    fun sembrar(
        onOk: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        postsCollection.get()
            .addOnSuccessListener { resultado ->
                if (!resultado.isEmpty) {
                    onOk()
                    return@addOnSuccessListener
                }

                var pendientes = posts.size
                var hayError = false

                posts.forEach { seed ->
                    val doc = hashMapOf(
                        "authorId"   to "system",
                        "authorName" to seed.authorName,
                        "authorRole" to seed.authorRole,
                        "title"      to seed.titulo,
                        "content"    to seed.contenido,
                        "type"       to seed.tipo,
                        "isPinned"   to seed.isPinned,
                        "createdAt"  to seed.createdAt
                    )

                    postsCollection.add(doc)
                        .addOnSuccessListener {
                            pendientes--
                            if (pendientes == 0 && !hayError) onOk()
                        }
                        .addOnFailureListener { e ->
                            if (!hayError) {
                                hayError = true
                                onError(e.message ?: "Error al sembrar posts")
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al comprobar colección")
            }
    }
}