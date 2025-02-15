package com.es.API_REST_SEGURA_2.controller

import com.es.API_REST_SEGURA_2.model.Tarea
import com.es.API_REST_SEGURA_2.service.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tarea")
class TareaController {

    @Autowired private lateinit var tareaService: TareaService

    @GetMapping()
    fun getTareas(){

    }

    @GetMapping("/{id}")
    fun getTarea(@PathVariable id: Int){

    }

    @PostMapping()
    fun postTarea(@RequestBody tarea: Tarea){

    }

    @PostMapping("/{usuario}")
    fun postTareaUsuario(@RequestBody tarea: Tarea){

    }

    @PostMapping("/{id}")
    fun updateTarea(@RequestBody tarea: Tarea){

    }

    @DeleteMapping("/{id}")
    fun deleteTarea(@PathVariable id: Int){

    }



}