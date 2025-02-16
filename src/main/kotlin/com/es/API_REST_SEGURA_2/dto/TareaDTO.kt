package com.es.API_REST_SEGURA_2.dto

import java.util.Date

data class TareaDTO(
    val titulo: String,
    val descripcion: String,
    val usuario: String,
    val fechaCreacion: Date,
    val estado: Boolean
)
