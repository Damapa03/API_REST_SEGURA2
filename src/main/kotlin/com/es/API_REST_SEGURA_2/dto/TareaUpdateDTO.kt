package com.es.API_REST_SEGURA_2.dto

data class TareaUpdateDTO(
    val titulo: String?,
    val descripcion: String?,
    val usuario: String?,
    val estado: Boolean?
)
