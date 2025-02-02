package org.example.Repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Model.Usuario
import org.example.Model.Direccion

class UsuarioRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Document> = database.getCollection("ColeccionUsuarios")

    fun registrarUsuario(cliente: Usuario) {
        val usuarioDoc = Document("email", cliente._id)
            .append("nombre", cliente.nombre)
            .append("nick", cliente.nick)
            .append("baneado", cliente.baneado)
            .append("activo", cliente.activo)
            .append("telefono", cliente.telefono)
            .append("direccion", cliente.direccion?.let {
                Document("calle", it.calle)
                    .append("num", it.num)
                    .append("cp", it.cp)
                    .append("ciudad", it.ciudad)
            })
        collection.insertOne(usuarioDoc)
    }

    fun obtenerUsuarioPorEmail(email: String): Usuario? {
        val document = collection.find(Document("email", email)).first()
        return document?.let {
            Usuario(
                _id = it.getString("email"),
                nombre = it.getString("nombre"),
                nick = it.getString("nick"),
                baneado = it.getBoolean("baneado"),
                activo = it.getBoolean("activo"),
                telefono = it.getList("telefono", String::class.java),
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
        val update = Document("\$set", Document("baneado", baneado))
        val result = collection.updateOne(Document("email", email), update)
        if (result.modifiedCount > 0) {
            println("Estado de usuario con correo $email actualizado a baneado: $baneado")
        } else {
            println("No se encontró el usuario con correo $email o no se pudo actualizar.")
        }
    }

    fun validarNickUnico(nick: String): Boolean {
        return collection.find(Document("nick", nick)).first() == null
    }
}