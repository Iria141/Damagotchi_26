package com.example.damagotchi_26.data

data class Post(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorRole: String = "", // "admin" o "jugador"
    val title: String = "",
    val content: String = "",
    val type: String = "", // "anuncio", "pregunta", "opinion"
    val createdAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)