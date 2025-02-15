package com.es.API_REST_SEGURA_2.model

data class Direccion(
    val calle: String,
    val num: String,
    val cp: String,
    val provincia: String,
    val municipio: String
)
