package org.example

import org.example.Model.*
import org.example.Repository.ComentariosRepository
import org.example.Repository.NoticiaRepository
import org.example.Repository.UsuarioRepository
import org.example.Service.*

fun main() {

    // Conexión a la base de datos
    val conexion = Conexion()  // Crea una instancia de la clase Conexion
    val mongoClient = conexion.mongoClient  // Obtiene el cliente de MongoDB
    val database = mongoClient.getDatabase(conexion.databaseName)  // Obtiene la base de datos

    // Inicialización de los repositorios
    val usuarioRepository = UsuarioRepository(database)
    val noticiaRepository = NoticiaRepository(database)
    val comentariosRepository = ComentariosRepository(database)

    // Creación de servicios, inyectando los repositorios
    val usuarioService = UsuarioService(usuarioRepository)
    val noticiaService = NoticiaService(noticiaRepository, usuarioRepository)
    val comentarioService = ComentariosService(comentariosRepository, usuarioRepository)

    // Credenciales del administrador
    val adminCorreo = "pruebaadmin@admin.com"
    val adminContrasena = "admin"

    while (true) {

        println("Seleccione una opción:")
        println("1. Registrar Usuarios")
        println("2. Publicar Noticias")
        println("3. Publicar Comentarios")
        println("4. Listar noticias publicadas por un usuario")
        println("5. Listar los comentarios de una noticia")
        println("6. Buscar noticias por etiquetas")
        println("7. Listar las últimas 10 noticias publicadas")
        println("8. Cambiar estado de usuario (ADMINS)")
        println("0. Salir")

        when (readlnOrNull()) {
            "1" -> {
                // Registrar usuario
                println("Ingrese el correo del usuario:")
                val correo = readln()
                println("Ingrese el nombre completo del usuario:")
                val nombre = readln()
                println("Ingrese el nick de usuario:")
                val nick = readln()

                // Captura de números de teléfono en una lista
                val telefonos = mutableListOf<String>()
                while (true) {
                    println("Ingrese un teléfono (o 'fin' para terminar):")
                    val telefono = readln()
                    if (telefono.lowercase() == "fin") break
                    telefonos.add(telefono)
                }

                // Captura de la dirección del usuario
                println("Ingrese el nombre de la calle:")
                val calle = readln()
                println("Ingrese el número de la calle:")
                val num = readln()
                println("Ingrese el código postal:")
                val cp = readln()
                println("Ingrese la ciudad:")
                val ciudad = readln()
                val direccion = Direccion(calle, num, cp, ciudad)

                // Creación del objeto Usuario y registro, activo y no baneado
                val usuario = Usuario(correo, nombre, nick, baneado = false, activo = true, telefonos, direccion)
                usuarioService.registrarUsuario(usuario)
            }
            "2" -> {
                // Publicar noticia
                println("Ingrese el título de la noticia:")
                val titulo = readln()
                println("Ingrese el cuerpo de la noticia:")
                val cuerpo = readln()
                println("Ingrese el nombre del autor (correo del usuario):")
                val autor = readln()
                println("Ingrese las etiquetas de la noticia (separadas por coma):")
                val tags = readln().split(",").map { it.trim() }

                // Creación del objeto Noticia y publicación
                val noticia = Noticia(titulo = titulo, cuerpo = cuerpo, autor = autor, tags = tags)
                noticiaService.publicarNoticia(noticia)
            }
            "3" -> {
                // Publicar comentario
                println("Ingrese el nombre del usuario que comenta (correo):")
                val usuario = readln()
                println("Ingrese el título de la noticia en la que va a comentar:")
                val tituloNoticia = readln()
                println("Ingrese el texto del comentario:")
                val texto = readln()

                // Creación del objeto Comentario y publicación
                val comentario = Comentario(usuario = usuario, noticiaId = tituloNoticia, texto = texto)
                comentarioService.publicarComentario(comentario)
            }
            "4" -> {
                // Listar noticias publicadas por un usuario
                println("Ingrese el correo del usuario:")
                val usuarioCorreo = readln()
                noticiaService.listarNoticiasPorUsuario(usuarioCorreo)
            }
            "5" -> {
                // Listar los comentarios de una noticia
                println("Ingrese el ID de la noticia:")
                val noticiaId = readln()
                comentarioService.listarComentariosDeNoticia(noticiaId)
            }
            "6" -> {
                // Buscar noticias por etiquetas
                println("Ingrese la etiqueta a buscar:")
                val etiqueta = readln()
                noticiaService.buscarNoticiasPorEtiqueta(etiqueta)
            }
            "7" -> {
                // Listar las últimas 10 noticias publicadas
                noticiaService.listarUltimasNoticias()
            }
            "8" -> {
                // Cambiar estado de un usuario con las credenciales del admin
                println("Ingrese el correo del administrador:")
                val correoAdmin = readln()
                if (correoAdmin == adminCorreo) {
                    println("Ingrese la contraseña del administrador:")
                    val contrasenaAdmin = readln()
                    if (contrasenaAdmin == adminContrasena) {
                        println("Ingrese el correo del usuario a cambiar de estado:")
                        val correoUsuario = readln()
                        println("¿Desea banear o desbanear al usuario? (ban/unban):")
                        val accion = readln()

                        when (accion.lowercase()) {
                            "ban" -> usuarioService.banearUsuario(correoUsuario)
                            "unban" -> usuarioService.desbanearUsuario(correoUsuario)
                            else -> println("Acción no válida.")
                        }
                    } else {
                        println("Contraseña incorrecta. Acceso denegado.")
                    }
                } else {
                    println("Correo de administrador incorrecto. Acceso denegado.")
                }
            }
            "0" -> {
                println("Saliendo...")
                break
            }
            else -> {
                println("Opción no válida, intente de nuevo.")
            }
        }
    }
}