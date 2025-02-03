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
            val comentarioConId = comentario.copy(noticiaId = noticiaId) // Se crea una copia del comentario con el ID real de la noticia
            comentariosRepository.publicarComentario(comentarioConId)
            println("Comentario publicado correctamente.")
        } else {
            println("No se encontró la noticia con el título ${comentario.noticiaId}.")
        }
    }

    fun listarComentariosDeNoticia(noticiaId: String) {
        val comentarios = comentariosRepository.listarComentariosDeNoticia(noticiaId) // Se obtiene la lista de comentarios asociados a la noticia
        if (comentarios.isNotEmpty()) { // Si hay comentarios, se imprimen en la consola
            comentarios.forEach { println(it) }
        } else {
            println("No hay comentarios para esta noticia.")
        }
    }
}
