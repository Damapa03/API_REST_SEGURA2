package com.es.API_REST_SEGURA_2.service

import com.es.API_REST_SEGURA_2.dto.TareaDTO
import com.es.API_REST_SEGURA_2.dto.TareaInsertDTO
import com.es.API_REST_SEGURA_2.dto.TareaUpdateDTO
import com.es.API_REST_SEGURA_2.error.exception.BadRequestException
import com.es.API_REST_SEGURA_2.error.exception.NotFoundException
import com.es.API_REST_SEGURA_2.model.Tarea
import com.es.API_REST_SEGURA_2.repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class TareaService {

    @Autowired private lateinit var tareaRepository: TareaRepository
    @Autowired private lateinit var usuarioService: UsuarioService

    fun getTareas(): List<Tarea> {
        return tareaRepository.findAll()
    }
    fun getTareaByUsuario(user: String): Tarea? {
        val tarea = tareaRepository.findByUsuario(user).orElseThrow { NotFoundException("la tarea no existe") }

        return tarea
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
        if ("USER" in rol){
            if (tarea.usuario != usuario){
                tareaEntity.usuario = usuario
            }
        }else {
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

    fun updateTarea(tareaModified: TareaUpdateDTO): Tarea {
        val tarea = getTareaByUsuario(tareaModified._id)

        tarea!!.copy(
            titulo = tareaModified.titulo?.takeIf { it.isNotBlank() } ?: tarea.titulo,
            descripcion = tareaModified.descripcion?.takeIf { it.isNotBlank() } ?: tarea.descripcion,
            usuario = tareaModified.usuario?.takeIf { it.isNotBlank() } ?: tarea.usuario,
            estado = tareaModified.estado ?: tarea.estado
        )

        return tareaRepository.save(tarea)
    }
   fun deleteTarea(id: String){
       try{ tareaRepository.deleteById(id)
       }catch (ex: Exception ){
           throw NotFoundException("tarea no encontrada")
       }
   }
}