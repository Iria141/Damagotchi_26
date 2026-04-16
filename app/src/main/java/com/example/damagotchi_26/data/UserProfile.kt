package com.example.damagotchi_26.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class UserProfile(
    val nombre: String = "",
    val fechaNacimiento: String = "",
    val rol: String = "",
    val fechaUltimaRegla: Long = 0L,
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

fun getUserProfile(
    uid: String,
    onResult: (UserProfile?, String?) -> Unit
) {
    val db = Firebase.firestore

    db.collection("users")
        .document(uid)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val profile = document.toObject(UserProfile::class.java)
                onResult(profile, null)
            } else {
                onResult(null, "No existe el perfil del usuario")
            }
        }
        .addOnFailureListener { e ->
            onResult(null, e.message)
        }



}
