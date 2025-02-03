package org.example.Service

import org.example.Model.Comentario
import org.example.Repository.ComentariosRepository
import org.example.Repository.UsuarioRepository


class ComentariosService(
    private val comentariosRepository: ComentariosRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun publicarComentario(comentario: Comentario) {
        // Obtener el usuario por correo
        val usuario = usuarioRepository.obtenerUsuarioPorEmail(comentario.usuario)

        // Verificar si el usuario existe
        if (usuario == null) {
            println("El usuario con correo ${comentario.usuario} no existe.")
            return
        }

        // Verificar si el usuario está baneado
        if (usuario.baneado) {
            println("El usuario ${comentario.usuario} está baneado y no puede publicar comentarios.")
            return
        }

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

    fun listarComentariosDeNoticia(tituloNoticia: String) {

        val noticiaId = comentariosRepository.obtenerNoticiaIdPorTitulo(tituloNoticia)

        if (noticiaId != null) {
            val comentarios = comentariosRepository.listarComentariosDeNoticia(noticiaId) // Se obtiene la lista de comentarios asociados a la noticia
            if (comentarios.isNotEmpty()) { // Si hay comentarios, se imprimen en la consola
                comentarios.forEach { println(it) }
            } else {
                println("No hay comentarios para la noticia con título '$tituloNoticia'.")
            }
        } else {
            println("No se encontró una noticia con el título '$tituloNoticia'.")
        }
    }
}
