package org.example.Service

import org.example.Model.Comentario
import org.example.Repository.ComentariosRepository


class ComentariosService(
    private val comentariosRepository: ComentariosRepository,
) {

    fun publicarComentario(comentario: Comentario) {
        // Buscar la noticia por el título
        val noticiaId = comentariosRepository.obtenerNoticiaIdPorTitulo(comentario.noticiaId)

        // Si se encuentra la noticia, se publica el comentario
        if (noticiaId != null) {
            val comentarioConId = comentario.copy(noticiaId = noticiaId)
            comentariosRepository.publicarComentario(comentarioConId)
            println("Comentario publicado correctamente.")
        } else {
            println("No se encontró la noticia con el título ${comentario.noticiaId}.")
        }
    }

    fun listarComentariosDeNoticia(noticiaId: String) {
        val comentarios = comentariosRepository.listarComentariosDeNoticia(noticiaId)
        if (comentarios.isNotEmpty()) {
            comentarios.forEach { println(it) }
        } else {
            println("No hay comentarios para esta noticia.")
        }
    }
}
