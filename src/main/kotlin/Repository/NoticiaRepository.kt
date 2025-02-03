package org.example.Repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Model.Noticia

class NoticiaRepository(private val database: MongoDatabase) {

    // Se obtiene la colección de noticias de la base de datos
    private val collection: MongoCollection<Document> = database.getCollection("ColeccionNoticias")

    fun publicarNoticia(noticia: Noticia) {
        // Se crea un documento BSON con los datos de la noticia
        val noticiaDoc = Document("titulo", noticia.titulo)
            .append("cuerpo", noticia.cuerpo)
            .append("fecha_pub", noticia.fecha_pub)
            .append("autor", noticia.autor)
            .append("tags", noticia.tags)

        collection.insertOne(noticiaDoc) // Se inserta el documento en la colección de noticias
    }

    fun listarNoticiasPorUsuario(usuarioCorreo: String): List<Noticia> {
        // Se filtran las noticias cuyo campo autor coincida con el correo proporcionado
        return collection.find(Document("autor", usuarioCorreo)).map { doc ->
            // Se convierte cada documento en un objeto Noticia
            Noticia(
                _id = doc.getObjectId("_id").toString(),
                titulo = doc.getString("titulo"),
                cuerpo = doc.getString("cuerpo"),
                fecha_pub = doc.getDate("fecha_pub"),
                autor = doc.getString("autor"),
                tags = doc.getList("tags", String::class.java)
            )
        }.toList()  // Se convierte el resultado en una lista
    }

    fun buscarNoticiasPorEtiqueta(etiqueta: String): List<Noticia> {
        // Se buscan noticias cuyo campo tags contenga la etiqueta proporcionada
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
        // Se ordenan las noticias por fecha de publicación en orden descendente y se limitan a 10
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