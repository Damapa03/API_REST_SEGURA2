package com.es.API_REST_SEGURA_2.service

import com.es.API_REST_SEGURA_2.dto.TareaDTO
import com.es.API_REST_SEGURA_2.dto.TareaInsertDTO
import com.es.API_REST_SEGURA_2.dto.TareaUpdateDTO
import com.es.API_REST_SEGURA_2.error.exception.BadRequestException
import com.es.API_REST_SEGURA_2.error.exception.ForbiddenException
import com.es.API_REST_SEGURA_2.error.exception.NotFoundException
import com.es.API_REST_SEGURA_2.model.Tarea
import com.es.API_REST_SEGURA_2.repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository
    @Autowired
    private lateinit var usuarioService: UsuarioService

    fun getTareas(): List<Tarea> {
        return tareaRepository.findAll()
    }

    fun getTareaByUsuario(user: String): Optional<List<Tarea>> {
        val auth = SecurityContextHolder.getContext().authentication
        var usuario = auth.name
        val rol = auth.authorities.map(GrantedAuthority::getAuthority)

        if ("ROLE_USER" in rol) {
            return tareaRepository.findByUsuario(usuario)
        } else return tareaRepository.findByUsuario(user)
    }

    fun getTareaById(id: String): Tarea? {
        return tareaRepository.findById(id).orElseThrow { NotFoundException("la tarea no existe") }
    }

    fun postTarea(tarea: TareaInsertDTO): TareaDTO {
        val auth = SecurityContextHolder.getContext().authentication
        var usuario = auth.name
        val rol = auth.authorities.map(GrantedAuthority::getAuthority)

        if (tarea.titulo.isBlank()) {
            throw BadRequestException("El titulo no puede estar en blanco")
        }
        var tareaEntity = Tarea(
            null,
            tarea.titulo,
            tarea.descripcion,
            tarea.usuario,
            Date.from(Instant.now()),
            false
        )
        if ("ROLE_USER" in rol) {
            if (tarea.usuario != usuario) {
                throw ForbiddenException("No tiene permiso para crear tareas a otro usuarios")
            }
        } else {
            usuarioService.getUsuarioByUser(tarea.usuario)
        }

        tareaRepository.save(tareaEntity)

        return TareaDTO(
            tareaEntity.titulo,
            tareaEntity.descripcion,
            tareaEntity.usuario,
            tareaEntity.fechaCreacion,
            tareaEntity.estado
        )

    }

    fun updateTarea(tareaModified: TareaUpdateDTO, id: String): Tarea {
        val tarea = getTareaById(id)
        val auth = SecurityContextHolder.getContext().authentication
        var usuario = auth.name
        val rol = auth.authorities.map(GrantedAuthority::getAuthority)

        val tareaUpdate = Tarea(
            _id = tarea!!._id,
            titulo = tareaModified.titulo?.takeIf { it.isNotBlank() } ?: tarea.titulo,
            descripcion = tareaModified.descripcion?.takeIf { it.isNotBlank() } ?: tarea.descripcion,
            usuario = tareaModified.usuario?.takeIf { it.isNotBlank() } ?: tarea.usuario,
            fechaCreacion = tarea.fechaCreacion,
            estado = tareaModified.estado ?: tarea.estado
        )
        if ("ROLE_USER" in rol) {
            if (tarea.usuario != usuario) {
                throw ForbiddenException("No tiene permiso para modificar tareas de otros usuarios")
            }
            if (tareaUpdate.usuario != usuario) {
                tareaUpdate.usuario = usuario
            }
        } else {
            usuarioService.getUsuarioByUser(tareaUpdate.usuario)
        }

        return tareaRepository.save(tareaUpdate)
    }

    fun deleteTarea(id: String) {
        val tarea = getTareaById(id)
        val auth = SecurityContextHolder.getContext().authentication
        var usuario = auth.name
        val rol = auth.authorities.map(GrantedAuthority::getAuthority)

        if ("ROLE_ADMIN" in rol) {
            tareaRepository.deleteById(id)
        } else if ("ROLE_USER" in rol && tarea!!.usuario == usuario) {
            tareaRepository.deleteById(id)
        } else throw ForbiddenException("No tiene permiso para borrar esa tarea")

    }
}