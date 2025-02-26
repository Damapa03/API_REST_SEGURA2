package com.es.API_REST_SEGURA_2.error.exception

class ForbiddenException (message: String): Exception("ForbiddenException (403). $message") {
}