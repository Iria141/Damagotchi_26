package com.example.damagotchi_26.repository

import com.example.damagotchi_26.data.PublicacionInformativa
import com.google.firebase.firestore.FirebaseFirestore

class PublicacionesInformativasRepository {

    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("publicacionesInfo")

    fun obtenerPublicaciones(
        onResultado: (List<PublicacionInformativa>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        coleccion
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.documents.mapNotNull { doc ->
                    doc.toObject(PublicacionInformativa::class.java)?.copy(id = doc.id)
                }
                onResultado(lista)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }
}