package com.example.damagotchi_26.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damagotchi_26.data.Comment
import com.example.damagotchi_26.data.Post
import com.example.damagotchi_26.data.PublicacionInformativa
import com.example.damagotchi_26.data.SeguimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeguimientoViewModel(
    private val repository: SeguimientoRepository = SeguimientoRepository()
) : ViewModel() {

    private val _publicaciones = MutableStateFlow<List<PublicacionInformativa>>(emptyList())
    val publicaciones: StateFlow<List<PublicacionInformativa>> = _publicaciones.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _comentarios = MutableStateFlow<List<Comment>>(emptyList())
    val comentarios: StateFlow<List<Comment>> = _comentarios.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cargarPublicaciones(semanaActual: Int, rolUsuario: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _publicaciones.value =
                    repository.obtenerPublicacionesInformativas(semanaActual, rolUsuario)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar publicaciones"
            } finally {
                _loading.value = false
            }
        }
    }

    fun cargarPosts() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _posts.value = repository.obtenerPosts()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar posts"
            } finally {
                _loading.value = false
            }
        }
    }

    fun publicarPost(
        authorId: String,
        authorName: String,
        authorRole: String,
        title: String,
        content: String,
        type: String
    ) {
        viewModelScope.launch {
            try {
                repository.guardarPost(
                    Post(
                        authorId = authorId,
                        authorName = authorName,
                        authorRole = authorRole,
                        title = title,
                        content = content,
                        type = type
                    )
                )
                cargarPosts()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al publicar post"
            }
        }
    }

    fun cargarComentarios(postId: String) {
        viewModelScope.launch {
            try {
                _comentarios.value = repository.obtenerComentarios(postId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar comentarios"
            }
        }
    }

    fun publicarComentario(
        postId: String,
        authorId: String,
        authorName: String,
        authorRole: String,
        content: String
    ) {
        viewModelScope.launch {
            try {
                repository.guardarComentario(
                    Comment(
                        postId = postId,
                        authorId = authorId,
                        authorName = authorName,
                        authorRole = authorRole,
                        content = content
                    )
                )
                cargarComentarios(postId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al publicar comentario"
            }
        }
    }
}