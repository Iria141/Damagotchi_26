package com.example.damagotchi_26.repository

import com.example.damagotchi_26.data.PublicacionInformativa
import com.google.firebase.firestore.FirebaseFirestore

class PublicacionesInformativasRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("publicaciones_informativas")

    fun obtenerPublicaciones(
        onResultado: (List<PublicacionInformativa>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        collection
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull { doc ->
                    doc.toObject(PublicacionInformativa::class.java)?.copy(id = doc.id)
                }
                onResultado(lista)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    fun agregarPublicacion(
        publicacion: PublicacionInformativa,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        collection
            .add(publicacion)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al guardar publicación") }
    }
}