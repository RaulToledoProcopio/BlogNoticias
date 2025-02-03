package org.example.Model

import java.util.Date

data class Noticia(
    val _id: String? = null,
    val titulo: String,
    val cuerpo: String,
    val fecha_pub: Date = Date(),
    val tags: List<String> = listOf(),
    val autor: String
)