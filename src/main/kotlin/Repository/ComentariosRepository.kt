package org.example.Repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Model.Comentario

class ComentariosRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Document> = database.getCollection("ColeccionComentarios")

    fun publicarComentario(comentario: Comentario) {
        val comentarioDoc = Document("usuario", comentario.usuario)
            .append("noticiaId", comentario.noticiaId)
            .append("texto", comentario.texto)
            .append("fecha", comentario.fecha)
        collection.insertOne(comentarioDoc)
    }

    fun listarComentariosDeNoticia(noticiaId: String): List<Comentario> {
        return collection.find(Document("noticiaId", noticiaId)).map { doc ->
            Comentario(
                _id = doc.getObjectId("_id").toString(),
                usuario = doc.getString("usuario"),
                noticiaId = doc.getString("noticiaId"),
                texto = doc.getString("texto"),
                fecha = doc.getDate("fecha")
            )
        }.toList()
    }

    // Buscar noticia por título
    fun obtenerNoticiaIdPorTitulo(titulo: String): String? {
        val noticiaCollection = database.getCollection("ColeccionNoticias")
        val noticiaDoc = noticiaCollection.find(Document("titulo", titulo)).first()
        return noticiaDoc?.getObjectId("_id")?.toString()
    }
}