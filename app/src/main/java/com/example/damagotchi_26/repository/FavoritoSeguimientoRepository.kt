package com.example.damagotchi_26.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritoSeguimientoRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun obtenerFavoritos(
        onResultado: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("usuarios")
            .document(uid)
            .collection("favoritos_seguimiento")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.map { it.id }
                onResultado(lista)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al cargar favoritos")
            }
    }

    fun agregarFavorito(
        idPublicacion: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val data = mapOf(
            "idPublicacion" to idPublicacion,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("usuarios")
            .document(uid)
            .collection("favoritos_seguimiento")
            .document(idPublicacion)
            .set(data)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al guardar favorito")
            }
    }

    fun quitarFavorito(
        idPublicacion: String,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("usuarios")
            .document(uid)
            .collection("favoritos_seguimiento")
            .document(idPublicacion)
            .delete()
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al eliminar favorito")
            }
    }
}