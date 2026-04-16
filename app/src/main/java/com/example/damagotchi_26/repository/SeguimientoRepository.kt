package com.example.damagotchi_26.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SeguimientoRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val publicacionesRef = db.collection("seguimiento_publicaciones")
    private val postsRef = db.collection("seguimiento_posts")
    private val commentsRef = db.collection("seguimiento_comments")

    suspend fun obtenerPublicacionesInformativas(
        semanaActual: Int,
        rolUsuario: String
    ): List<PublicacionInformativa> {
        return publicacionesRef
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(PublicacionInformativa::class.java)?.copy(id = doc.id)
            }
            .filter { pub ->
                semanaActual in pub.semanaInicio..pub.semanaFin &&
                        (pub.rolDestino.isEmpty() || pub.rolDestino.contains(rolUsuario))
            }
            .sortedByDescending { it.destacada }
    }

    suspend fun guardarPublicacionInformativa(publicacion: PublicacionInformativa) {
        if (publicacion.id.isBlank()) {
            publicacionesRef.add(publicacion).await()
        } else {
            publicacionesRef.document(publicacion.id).set(publicacion).await()
        }
    }

    suspend fun obtenerPosts(): List<Post> {
        return postsRef
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(id = doc.id)
            }
            .sortedByDescending { it.createdAt }
    }

    suspend fun guardarPost(post: Post) {
        if (post.id.isBlank()) {
            postsRef.add(post).await()
        } else {
            postsRef.document(post.id).set(post).await()
        }
    }

    suspend fun obtenerComentarios(postId: String): List<Comment> {
        return commentsRef
            .whereEqualTo("postId", postId)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Comment::class.java)?.copy(id = doc.id)
            }
            .sortedBy { it.createdAt }
    }

    suspend fun guardarComentario(comment: Comment) {
        if (comment.id.isBlank()) {
            commentsRef.add(comment).await()
        } else {
            commentsRef.document(comment.id).set(comment).await()
        }
    }
}