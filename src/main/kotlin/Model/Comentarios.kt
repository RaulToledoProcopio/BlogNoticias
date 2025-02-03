package org.example.Model

import java.util.Date

data class Comentario(
    val _id: String? = null,
    val usuario: String,
    val noticiaId: String,
    val texto: String,
    val fecha: Date = Date()
)
