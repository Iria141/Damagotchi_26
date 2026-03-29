package com.example.damagotchi_26.data

data class Comment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorRole: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis()
)