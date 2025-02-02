package org.example.Repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Model.Noticia

class NoticiaRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Document> = database.getCollection("ColeccionNoticias")

    fun publicarNoticia(noticia: Noticia) {
        val noticiaDoc = Document("titulo", noticia.titulo)
            .append("cuerpo", noticia.cuerpo)
            .append("fecha_pub", noticia.fecha_pub)
            .append("autor", noticia.autor)
            .append("tags", noticia.tags)
        collection.insertOne(noticiaDoc)
    }

    fun listarNoticiasPorUsuario(usuarioCorreo: String): List<Noticia> {
        return collection.find(Document("autor", usuarioCorreo)).map { doc ->
            Noticia(
                _id = doc.getObjectId("_id").toString(),
                titulo = doc.getString("titulo"),
                cuerpo = doc.getString("cuerpo"),
                fecha_pub = doc.getDate("fecha_pub"),
                autor = doc.getString("autor"),
                tags = doc.getList("tags", String::class.java)
            )
        }.toList()
    }

    fun buscarNoticiasPorEtiqueta(etiqueta: String): List<Noticia> {
        return collection.find(Document("tags", etiqueta)).map { doc ->
            Noticia(
                _id = doc.getObjectId("_id").toString(),
                titulo = doc.getString("titulo"),
                cuerpo = doc.getString("cuerpo"),
                fecha_pub = doc.getDate("fecha_pub"),
                autor = doc.getString("autor"),
                tags = doc.getList("tags", String::class.java)
            )
        }.toList()
    }

    fun listarUltimasNoticias(): List<Noticia> {
        return collection.find().sort(Document("fecha_pub", -1)).limit(10).map { doc ->
            Noticia(
                _id = doc.getObjectId("_id").toString(),
                titulo = doc.getString("titulo"),
                cuerpo = doc.getString("cuerpo"),
                fecha_pub = doc.getDate("fecha_pub"),
                autor = doc.getString("autor"),
                tags = doc.getList("tags", String::class.java)
            )
        }.toList()
    }
}
