package org.example.Service

import org.example.Model.Noticia
import org.example.Repository.NoticiaRepository
import org.example.Repository.UsuarioRepository

class NoticiaService(
    private val noticiaRepository: NoticiaRepository,
    private val usuarioRepository: UsuarioRepository // Necesitamos verificar el estado del usuario
) {

    fun publicarNoticia(noticia: Noticia) {
        // Obtener el usuario por correo (autor de la noticia)
        val usuario = usuarioRepository.obtenerUsuarioPorEmail(noticia.autor)

        // Verificar si el usuario existe
        if (usuario == null) {
            println("El usuario con correo ${noticia.autor} no existe.")
            return
        }

        // Verificar si el usuario está baneado
        if (usuario.baneado) {
            println("El usuario ${noticia.autor} está baneado y no puede publicar noticias.")
            return
        }

        // Si el usuario está bien, permitimos la publicación de la noticia
        noticiaRepository.publicarNoticia(noticia)
        println("Noticia publicada correctamente.")
    }

    fun listarNoticiasPorUsuario(usuarioCorreo: String) {
        val noticias = noticiaRepository.listarNoticiasPorUsuario(usuarioCorreo)
        if (noticias.isNotEmpty()) {
            noticias.forEach { println(it) }
        } else {
            println("No se encontraron noticias para el usuario $usuarioCorreo.")
        }
    }

    fun buscarNoticiasPorEtiqueta(etiqueta: String) {
        val noticias = noticiaRepository.buscarNoticiasPorEtiqueta(etiqueta)
        if (noticias.isNotEmpty()) {
            noticias.forEach { println(it) }
        } else {
            println("No se encontraron noticias con la etiqueta '$etiqueta'.")
        }
    }

    fun listarUltimasNoticias() {
        val noticias = noticiaRepository.listarUltimasNoticias()
        noticias.forEach { println(it) }
    }
}