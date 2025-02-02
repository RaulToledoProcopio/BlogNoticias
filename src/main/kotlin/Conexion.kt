package org.example

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import io.github.cdimascio.dotenv.dotenv

class Conexion {

    private val dotenv = dotenv() // Cargar dotenv
    private val connectString: String = dotenv["URL_MONGODB"] // Obtener la URL desde el archivo .env
    val mongoClient: MongoClient = MongoClients.create(connectString) // Crear cliente MongoDB
    val databaseName: String = "EjercicioNoticias" // Nombre de la base de datos
}
