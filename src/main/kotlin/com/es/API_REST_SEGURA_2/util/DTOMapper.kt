package com.es.API_REST_SEGURA_2.util

import com.es.API_REST_SEGURA_2.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA_2.model.Usuario


object DTOMapper {

    fun userDTOToEntity(usuarioInsertDTO: UsuarioRegisterDTO): Usuario {
        return Usuario(
            _id = null,
            username = usuarioInsertDTO.username,
            password = usuarioInsertDTO.password,
            roles = usuarioInsertDTO.rol ?: "USER",
            direccion = usuarioInsertDTO.direccion
        )
    }
//
//    fun userEntityToDTO(usuario: Usuario): UsuarioDTO {
//
//        return UsuarioDTO(
//            username = usuario.username,
//            email = usuario.email,
//            rol = usuario.roles,
//        )
//
//    }
//
//    fun listOfUserEntitiesToDTO(usuarios: List<Usuario>): List<UsuarioDTO> {
//        return usuarios.map {
//            UsuarioDTO(
//                username = it.username,
//                email = it.email,
//                rol = it.roles,
//            )
//        }
//
//    }
}