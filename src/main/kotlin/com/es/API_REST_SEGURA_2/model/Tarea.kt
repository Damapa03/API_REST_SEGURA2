package com.es.API_REST_SEGURA_2.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("Tarea")
data class Tarea(
    @BsonId
    val _id: String,
    val titulo: String,
    val descripcion: String,
    val usuario: String,
    val fechaCreacion: Date,
    val estado: Boolean

)
