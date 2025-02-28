package org.example.Repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Model.Usuario
import org.example.Model.Direccion

class UsuarioRepository(private val database: MongoDatabase) {

    // Se obtiene la colección ColeccionUsuarios de la base de datos
    private val collection: MongoCollection<Document> = database.getCollection("ColeccionUsuarios")

    fun registrarUsuario(cliente: Usuario) {
        // Se crea un documento BSON con los datos del usuario
        val usuarioDoc = Document("email", cliente._id)
            .append("nombre", cliente.nombre)
            .append("nick", cliente.nick)
            .append("baneado", cliente.baneado)
            .append("activo", cliente.activo)
            .append("telefono", cliente.telefono)

            // Si la dirección no es nula, se guarda como un subdocumento dentro del usuario
            .append("direccion", cliente.direccion?.let {
                Document("calle", it.calle)
                    .append("num", it.num)
                    .append("cp", it.cp)
                    .append("ciudad", it.ciudad)
            })

        collection.insertOne(usuarioDoc) // Se inserta el documento en la colección de usuarios
    }

    fun obtenerUsuarioPorEmail(email: String): Usuario? {
        // Se busca en la colección el documento cuyo campo email coincida con el proporcionado
        val document = collection.find(Document("email", email)).first()

        // Si el usuario existe, se transforma el documento en un objeto Usuario y se devuelve
        return document?.let {
            Usuario(
                _id = it.getString("email"),
                nombre = it.getString("nombre"),
                nick = it.getString("nick"),
                baneado = it.getBoolean("baneado"),
                activo = it.getBoolean("activo"),
                telefono = it.getList("telefono", String::class.java),

                // Si el documento tiene una dirección, se transforma en un objeto Direccion
                direccion = it.get("direccion", Document::class.java)?.let { dir ->
                    Direccion(
                        calle = dir.getString("calle"),
                        num = dir.getString("num"),
                        cp = dir.getString("cp"),
                        ciudad = dir.getString("ciudad")
                    )
                }
            )
        }
    }

    fun actualizarEstadoUsuario(email: String, baneado: Boolean) {

        val update = Document("\$set", Document("baneado", baneado)) // Se crea un documento de actualización con el nuevo estado de baneo

        val result = collection.updateOne(Document("email", email), update) // Se intenta actualizar el usuario con el email proporcionado

        // Se verifica si se realizó alguna modificación en la base de datos
        if (result.modifiedCount <= 0) {
            println("No se encontró el usuario con correo $email o no se pudo actualizar.")
        }
    }

    fun validarNickUnico(nick: String): Boolean {
        // Se busca si existe un usuario con el nickname proporcionado
        return collection.find(Document("nick", nick)).first() == null
    }
}
