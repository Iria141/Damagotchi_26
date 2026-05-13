package com.example.damagotchi_26.repository

import com.example.damagotchi_26.data.Comment
import com.example.damagotchi_26.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PublicacionesRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("community_posts")

    fun obtenerPosts(
        onResultado: (List<Post>) -> Unit,
        onError: (String) -> Unit
    ) {
        postsCollection
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                }.sortedWith(compareByDescending<Post> { it.isPinned }.thenByDescending { it.createdAt })
                onResultado(lista)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar publicaciones")
            }
    }

    fun crearPost(
        titulo: String,
        contenido: String,
        tipo: String,
        authorName: String,
        authorRole: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        // Consulta el perfil para obtener alias si está activado
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val usarAlias = doc.getBoolean("usar_alias") ?: false
                val alias = doc.getString("alias") ?: ""
                val nombreFinal = if (usarAlias && alias.isNotBlank()) alias else authorName

                val post = hashMapOf(
                    "authorId" to uid,
                    "authorName" to nombreFinal,
                    "authorRole" to authorRole,
                    "title" to titulo,
                    "content" to contenido,
                    "type" to tipo,
                    "isPinned" to (authorRole.lowercase() == "admin" && tipo == "anuncio"),
                    "createdAt" to System.currentTimeMillis()
                )

                postsCollection
                    .add(post)
                    .addOnSuccessListener { onOk() }
                    .addOnFailureListener { e -> onError(e.message ?: "Error al crear publicación") }
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error al obtener perfil") }
    }

    fun eliminarPost(
        postId: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        postsCollection
            .document(postId)
            .delete()
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al eliminar publicación") }
    }
}

class ComentarioRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun comentariosRef(postId: String) =
        db.collection("community_posts").document(postId).collection("comentarios")

    fun obtenerComentarios(
        postId: String,
        onResultado: (List<Comment>) -> Unit,
        onError: (String) -> Unit
    ) {
        comentariosRef(postId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.copy(id = doc.id)
                }
                onResultado(lista)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar comentarios")
            }
    }

    fun agregarComentario(
        postId: String,
        texto: String,
        authorName: String,
        authorRole: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        // Consulta el perfil para obtener alias si está activado
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val usarAlias = doc.getBoolean("usar_alias") ?: false
                val alias = doc.getString("alias") ?: ""
                val nombreFinal = if (usarAlias && alias.isNotBlank()) alias else authorName

                val comentario = hashMapOf(
                    "postId" to postId,
                    "authorId" to uid,
                    "authorName" to nombreFinal,
                    "authorRole" to authorRole,
                    "content" to texto,
                    "createdAt" to System.currentTimeMillis()
                )

                comentariosRef(postId)
                    .add(comentario)
                    .addOnSuccessListener { onOk() }
                    .addOnFailureListener { e -> onError(e.message ?: "Error al enviar comentario") }
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error al obtener perfil") }
    }
}

class LikeRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun likesRef(postId: String) =
        db.collection("community_posts").document(postId).collection("likes")

    fun obtenerLikesPost(
        postId: String,
        onResultado: (Int, Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return

        likesRef(postId)
            .get()
            .addOnSuccessListener { result ->
                val total = result.size()
                val yaDioLike = result.documents.any { it.id == uid }
                onResultado(total, yaDioLike)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar likes")
            }
    }

    fun toggleLikePost(
        postId: String,
        yaDioLike: Boolean,
        onOk: (nuevoTotal: Int, nuevoLike: Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val docRef = likesRef(postId).document(uid)

        if (yaDioLike) {
            docRef.delete()
                .addOnSuccessListener {
                    likesRef(postId).get()
                        .addOnSuccessListener { onOk(it.size(), false) }
                        .addOnFailureListener { e -> onError(e.message ?: "Error") }
                }
                .addOnFailureListener { e -> onError(e.message ?: "Error al quitar like") }
        } else {
            docRef.set(
                mapOf(
                    "uid" to uid,
                    "timestamp" to System.currentTimeMillis()
                )
            )
                .addOnSuccessListener {
                    likesRef(postId).get()
                        .addOnSuccessListener { onOk(it.size(), true) }
                        .addOnFailureListener { e -> onError(e.message ?: "Error") }
                }
                .addOnFailureListener { e -> onError(e.message ?: "Error al dar like") }
        }
    }
}