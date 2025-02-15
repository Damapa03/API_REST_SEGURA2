package com.es.API_REST_SEGURA_2.controller

import com.es.API_REST_SEGURA_2.dto.LoginUsuarioDTO
import com.es.API_REST_SEGURA_2.dto.UsuarioDTO
import com.es.API_REST_SEGURA_2.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA_2.error.exception.UnauthorizedException
import com.es.API_REST_SEGURA_2.service.TokenService
import com.es.API_REST_SEGURA_2.service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuario")
class UsuarioController{

    @Autowired private lateinit var usuarioservice: UsuarioService
    @Autowired private lateinit var authenticationManager: AuthenticationManager
    @Autowired private lateinit var tokenService: TokenService

    @PostMapping("/register")
    fun register(
        @RequestBody usuarioRegisterDTO: UsuarioRegisterDTO
    ): ResponseEntity<UsuarioDTO> {
        val usuarioInsertadoDTO: UsuarioDTO = usuarioservice.insertUser(usuarioRegisterDTO)

        return ResponseEntity(usuarioInsertadoDTO, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(@RequestBody usuario: LoginUsuarioDTO) : ResponseEntity<Any>? {

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Credenciales incorrectas")
        }

        // SI PASAMOS LA AUTENTICACIÓN, SIGNIFICA QUE ESTAMOS BIEN AUTENTICADOS
        // PASAMOS A GENERAR EL TOKEN
        var token = tokenService.generarToken(authentication)

        return ResponseEntity(mapOf("token" to token), HttpStatus.CREATED)
    }
}