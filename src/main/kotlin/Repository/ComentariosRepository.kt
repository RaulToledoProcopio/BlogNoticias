package org.example.Repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Model.Comentario

class ComentariosRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Document> = database.getCollection("ColeccionComentarios")

    fun publicarComentario(comentario: Comentario) {
        // Se crea un documento BSON con los datos del comentario
        val comentarioDoc = Document("usuario", comentario.usuario)
            .append("noticiaId", comentario.noticiaId)
            .append("texto", comentario.texto)
            .append("fecha", comentario.fecha)
        collection.insertOne(comentarioDoc) // Se inserta el documento en la colección de comentarios
    }

    fun listarComentariosDeNoticia(noticiaId: String): List<Comentario> {

        // Se busca en la colección los comentarios cuyo "noticiaId" coincida con el proporcionado
        return collection.find(Document("noticiaId", noticiaId)).map { doc ->
            Comentario(
                _id = doc.getObjectId("_id").toString(),
                usuario = doc.getString("usuario"),
                noticiaId = doc.getString("noticiaId"),
                texto = doc.getString("texto"),
                fecha = doc.getDate("fecha")
            )
        }.toList() // Se convierte el resultado en una lista
    }

    // Buscar noticia por título
    fun obtenerNoticiaIdPorTitulo(titulo: String): String? {
        val noticiaCollection = database.getCollection("ColeccionNoticias") // Se accede a la colección de noticias en la base de datos
        val noticiaDoc = noticiaCollection.find(Document("titulo", titulo)).first() // Se busca el primer documento que tenga un campo "titulo" con el valor proporcionado
        return noticiaDoc?.getObjectId("_id")?.toString() // Se obtiene el ID de la noticia si existe, de lo contrario, se devuelve null
    }
}