package com.example.damagotchi_26.repository


import com.example.damagotchi_26.data.AnuncioSeguimiento
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

fun saveAnuncioSeguimiento(
    anuncio: AnuncioSeguimiento,
    onResult: (Boolean, String?) -> Unit
) {
    val db = Firebase.firestore

    db.collection("anuncios_seguimiento")
        .add(anuncio)
        .addOnSuccessListener {
            onResult(true, null)
        }
        .addOnFailureListener { e ->
            onResult(false, e.message)
        }
}

fun getAnunciosSeguimiento(
    onResult: (List<AnuncioSeguimiento>, String?) -> Unit
) {
    val db = Firebase.firestore

    db.collection("anuncios_seguimiento")
        .get()
        .addOnSuccessListener { result ->
            val anuncios = result.documents.mapNotNull { doc ->
                doc.toObject(AnuncioSeguimiento::class.java)
            }
            onResult(anuncios, null)
        }
        .addOnFailureListener { e ->
            onResult(emptyList(), e.message)
        }
}