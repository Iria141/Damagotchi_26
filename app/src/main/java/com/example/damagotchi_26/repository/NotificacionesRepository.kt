package com.example.damagotchi_26.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Notificacion(
    val id: String = "",
    val titulo: String = "",
    val cuerpo: String = "",
    val tipo: String = "",
    val postId: String = "",
    val leida: Boolean = false,
    val createdAt: Long = 0L
)

class NotificacionesRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun notificacionesRef(uid: String) =
        db.collection("users").document(uid).collection("notificaciones")

    fun obtenerNotificaciones(
        onResultado: (List<Notificacion>) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        notificacionesRef(uid)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull { doc ->
                    doc.toObject(Notificacion::class.java)?.copy(id = doc.id)
                }
                onResultado(lista)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar notificaciones")
            }
    }

    fun marcarComoLeida(
        notificacionId: String,
        onOk: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: return

        notificacionesRef(uid)
            .document(notificacionId)
            .update("leida", true)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e -> onError(e.message ?: "Error") }
    }

    fun marcarTodasComoLeidas(
        onOk: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: return

        notificacionesRef(uid)
            .whereEqualTo("leida", false)
            .get()
            .addOnSuccessListener { result ->
                val batch = db.batch()
                result.documents.forEach { doc ->
                    batch.update(doc.reference, "leida", true)
                }
                batch.commit()
                    .addOnSuccessListener { onOk() }
                    .addOnFailureListener { e -> onError(e.message ?: "Error") }
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error") }
    }
}