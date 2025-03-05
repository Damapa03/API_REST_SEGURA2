package com.es.API_REST_SEGURA_2

import com.es.API_REST_SEGURA_2.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(RSAKeysProperties::class)
@SpringBootApplication
class ApiRestSegura2Application

fun main(args: Array<String>) {
	runApplication<ApiRestSegura2Application>(*args)
}