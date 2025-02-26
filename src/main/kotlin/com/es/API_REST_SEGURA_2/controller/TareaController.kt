package com.es.API_REST_SEGURA_2.controller

import com.es.API_REST_SEGURA_2.dto.TareaDTO
import com.es.API_REST_SEGURA_2.dto.TareaInsertDTO
import com.es.API_REST_SEGURA_2.dto.TareaUpdateDTO
import com.es.API_REST_SEGURA_2.model.Tarea
import com.es.API_REST_SEGURA_2.service.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/tarea")
class TareaController {

    @Autowired private lateinit var tareaService: TareaService

    @GetMapping()
    fun getTareas(): ResponseEntity<List<Tarea>> {
        return ResponseEntity(tareaService.getTareas(), HttpStatus.OK)
    }

    @GetMapping("/{user}")
    fun getTarea(@PathVariable user: String): ResponseEntity<Optional<List<Tarea>>> {
        return ResponseEntity(tareaService.getTareaByUsuario(user), HttpStatus.OK)
    }

    @PostMapping()
    fun postTarea(@RequestBody tarea: TareaInsertDTO): ResponseEntity<TareaDTO> {
        return ResponseEntity(tareaService.postTarea(tarea), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateTarea(@RequestBody tarea: TareaUpdateDTO, @PathVariable id: String): ResponseEntity<Tarea> {

        val tareaUpdated = tareaService.updateTarea(tarea, id)
        return ResponseEntity(tareaUpdated, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteTarea(@PathVariable id: String): ResponseEntity<Any> {
        tareaService.deleteTarea(id)
        return ResponseEntity(HttpStatus.OK)
    }



}