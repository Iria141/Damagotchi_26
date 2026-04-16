package com.example.damagotchi_26.repository

import com.example.damagotchi_26.data.AnuncioSeguimiento
import com.google.firebase.firestore.FirebaseFirestore

private val db = FirebaseFirestore.getInstance()
private val anunciosCollection = db.collection("anuncios_seguimiento")

fun getAnunciosSeguimiento(
    onResult: (List<AnuncioSeguimiento>, String?) -> Unit
) {
    anunciosCollection
        .get()
        .addOnSuccessListener { result ->
            val lista = result.documents.mapNotNull { doc ->
                doc.toObject(AnuncioSeguimiento::class.java)?.copy(id = doc.id)
            }
            onResult(lista, null)
        }
        .addOnFailureListener { e ->
            onResult(emptyList(), e.message ?: "Error al cargar anuncios")
        }
}

fun agregarAnuncioSeguimiento(
    anuncio: AnuncioSeguimiento,
    onOk: () -> Unit,
    onError: (String) -> Unit
) {
    anunciosCollection
        .add(anuncio)
        .addOnSuccessListener { onOk() }
        .addOnFailureListener { e -> onError(e.message ?: "Error al guardar anuncio") }
}