package org.example.Model

import java.util.Date

data class Comentario(
    val _id: String? = null,
    val usuario: String, // Nombre del usuario que comentó
    val noticiaId: String, // ID de la noticia
    val texto: String,
    val fecha: Date = Date() // Fecha inmutable
)
