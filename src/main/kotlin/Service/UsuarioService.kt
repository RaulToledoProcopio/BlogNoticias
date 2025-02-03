package org.example.Service

import org.example.Model.Usuario
import org.example.Repository.UsuarioRepository

class UsuarioService(private val usuarioRepository: UsuarioRepository) {

    fun registrarUsuario(usuario: Usuario) {
        if (usuarioRepository.obtenerUsuarioPorEmail(usuario._id!!) != null) {
            println("Error: Ya existe un usuario con este email.")
            return
        }

        if (!usuarioRepository.validarNickUnico(usuario.nick)) {
            println("Error: El nombre de usuario ya está en uso.")
            return
        }

        usuarioRepository.registrarUsuario(usuario)
        println("Usuario registrado correctamente.")
    }


    fun banearUsuario(correo: String) {
        val usuario = usuarioRepository.obtenerUsuarioPorEmail(correo)
        if (usuario != null) {
            usuarioRepository.actualizarEstadoUsuario(correo, baneado = true)
            println("Usuario $correo ha sido baneado.")
        } else {
            println("No se encontró el usuario con correo $correo.")
        }
    }

    fun desbanearUsuario(correo: String) {
        val usuario = usuarioRepository.obtenerUsuarioPorEmail(correo)
        if (usuario != null) {
            usuarioRepository.actualizarEstadoUsuario(correo, baneado = false)
            println("Usuario $correo ha sido desbaneado.")
        } else {
            println("No se encontró el usuario con correo $correo.")
        }
    }
}
