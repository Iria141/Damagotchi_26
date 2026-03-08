package com.example.damagotchi_26.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class UserProfile(
    val nombre: String = "",
    val fechaNacimiento: String = "",
    val semanaGestacion: String = "",
    val sexoBebe: String = "",
    val email: String = ""
)

fun saveUserProfile(
    uid: String,
    profile: UserProfile,
    onResult: (Boolean, String?) -> Unit
) {
    val db = Firebase.firestore

    db.collection("users")
        .document(uid)
        .set(profile)
        .addOnSuccessListener {
            onResult(true, null)
        }
        .addOnFailureListener { e ->
            onResult(false, e.message)
        }
}