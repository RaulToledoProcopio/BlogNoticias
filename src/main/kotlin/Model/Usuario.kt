package org.example.Model

data class Usuario(
    val _id : String?,
    val nombre: String,
    val nick: String,
    val baneado: Boolean,
    val activo: Boolean,
    val telefono : List<String>,
    val direccion: Direccion?
)
