package com.example.damagotchi_26.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VisitaComunidadRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun obtenerUltimaVisita(
        onResultado: (Long) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResultado(0L)
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val ultimaVisita = document.getLong("ultimaVisitaComunidad") ?: 0L
                onResultado(ultimaVisita)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al obtener última visita")
            }
    }

    fun actualizarUltimaVisita(
        onOk: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("users")
            .document(uid)
            .update("ultimaVisitaComunidad", System.currentTimeMillis())
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al actualizar última visita")
            }
    }
}