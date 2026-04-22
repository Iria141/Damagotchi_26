package com.example.damagotchi_26.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LikesPublicacionRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun likesRef(postId: String) =
        db.collection("community_posts")
            .document(postId)
            .collection("likes")

    fun obtenerLikes(
        postId: String,
        onResultado: (total: Int, yoDiLike: Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        likesRef(postId)
            .get()
            .addOnSuccessListener { result ->
                val total = result.size()
                val yoDiLike = result.documents.any { it.id == uid }
                onResultado(total, yoDiLike)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar likes")
            }
    }

    fun darLike(
        postId: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val data = mapOf(
            "uid" to uid,
            "timestamp" to System.currentTimeMillis()
        )

        likesRef(postId)
            .document(uid)
            .set(data)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al dar like")
            }
    }

    fun quitarLike(
        postId: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        likesRef(postId)
            .document(uid)
            .delete()
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al quitar like")
            }
    }

    fun toggleLike(
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

        if (yaDioLike) {
            quitarLike(
                postId = postId,
                onOk = {
                    likesRef(postId).get()
                        .addOnSuccessListener { onOk(it.size(), false) }
                        .addOnFailureListener { e -> onError(e.message ?: "Error") }
                },
                onError = onError
            )
        } else {
            darLike(
                postId = postId,
                onOk = {
                    likesRef(postId).get()
                        .addOnSuccessListener { onOk(it.size(), true) }
                        .addOnFailureListener { e -> onError(e.message ?: "Error") }
                },
                onError = onError
            )
        }
    }

    fun obtenerPostsConLike(
        onResultado: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collectionGroup("likes")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->
                val postIds = result.documents.mapNotNull { doc ->
                    doc.reference.parent.parent?.id
                }
                onResultado(postIds)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar posts con like")
            }
    }
}