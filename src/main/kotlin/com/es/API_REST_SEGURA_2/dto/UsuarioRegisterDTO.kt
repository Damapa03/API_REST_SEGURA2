﻿package com.es.API_REST_SEGURA_2.dto

import com.es.API_REST_SEGURA_2.model.Direccion

data class UsuarioRegisterDTO(
    val username: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String? = "USER",
    val direccion: Direccion
)