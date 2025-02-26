package com.es.API_REST_SEGURA_2.service

import com.es.API_REST_SEGURA_2.dto.UsuarioDTO
import com.es.API_REST_SEGURA_2.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA_2.error.exception.BadRequestException
import com.es.API_REST_SEGURA_2.error.exception.NotFoundException
import com.es.API_REST_SEGURA_2.error.exception.UnauthorizedException
import com.es.API_REST_SEGURA_2.model.Usuario
import com.es.API_REST_SEGURA_2.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService: UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var externalApiService: ExternalApiService


    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO {

        // COMPROBACIONES
        // Comprobar si los campos vienen vacíos
        if (usuarioInsertadoDTO.username.isBlank()
            || usuarioInsertadoDTO.password.isBlank()
            || usuarioInsertadoDTO.passwordRepeat.isBlank()) {

            throw BadRequestException("Uno o más campos vacíos")
        }

        // Comprobamos que el usuario existe previamente
        if(usuarioRepository.findByUsername(usuarioInsertadoDTO.username).isPresent) {
            throw BadRequestException("Usuario ${usuarioInsertadoDTO.username} ya está registrado")
        }

        // comprobar que ambas passwords sean iguales
        if(usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) {
            throw BadRequestException("Las contrasenias no coinciden")
        }

        // Comprobar el ROL
        if(usuarioInsertadoDTO.rol != null && usuarioInsertadoDTO.rol != "USER" && usuarioInsertadoDTO.rol != "ADMIN" ) {
            throw BadRequestException("ROL: ${usuarioInsertadoDTO.rol} incorrecto")
        }


        // Comprobar la provincia
        val datosProvincias = externalApiService.obtenerProvinciasDesdeApi()
        var cpro: String = ""
        if(datosProvincias != null) {
            if(datosProvincias.data != null) {
                val provinciaEncontrada = datosProvincias.data.stream().filter {
                    it.PRO == usuarioInsertadoDTO.direccion.provincia.uppercase()
                }.findFirst().orElseThrow {
                    BadRequestException("Provincia ${usuarioInsertadoDTO.direccion.provincia} no encontrada")
                }
                cpro = provinciaEncontrada.CPRO
            }
        }

        // Comprobar el municipio
        val datosMunicipios = externalApiService.obtenerMunicipiosDesdeApi(cpro)
        datosMunicipios?.data?.stream()?.filter {
            it.DMUN50 == usuarioInsertadoDTO.direccion.municipio.uppercase()
        }?.findFirst()?.orElseThrow {
            BadRequestException("Municipio ${usuarioInsertadoDTO.direccion.municipio} incorrecto")
        }

        // Insertar el user
        val usuario = Usuario(
            null,
            usuarioInsertadoDTO.username,
            passwordEncoder.encode(usuarioInsertadoDTO.password),
            usuarioInsertadoDTO.rol,
            usuarioInsertadoDTO.direccion
        )

        // inserto en bd
        usuarioRepository.insert(usuario)

        // retorno un DTO
        return UsuarioDTO(
            usuario.username,
            usuario.roles
        )

    }

    fun getUsuarioByUser(username: String) {
        usuarioRepository.findByUsername(username).orElseThrow{ NotFoundException("Usuario $username not found") }
    }

    override fun loadUserByUsername(username: String?): UserDetails? {
        var usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }
}