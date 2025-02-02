package org.example.Model

import java.util.Date

data class Noticia(
    val _id: String? = null, // ID generado automáticamente
    val titulo: String,
    val cuerpo: String,
    val fecha_pub: Date = Date(), // Fecha inmutable
    val tags: List<String> = listOf(),
    val autor: String // ID del usuario que la escribió
)
