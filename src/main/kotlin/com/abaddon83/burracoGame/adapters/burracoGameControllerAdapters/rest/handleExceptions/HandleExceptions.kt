package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.handleExceptions


import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.ErrorMsgModule
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.errorsHandling() {



//    exception<ListEmptyException> { e ->
//        call.respond(HttpStatusCode.ServiceUnavailable, message = JsonErrorMsg(
//                code = HttpStatusCode.ServiceUnavailable.value.toString(),
//                message = e.message.orEmpty()
//        ))
//    }
//
//    exception<MarvelApiClientException> { e ->
//        call.respond(HttpStatusCode.NotFound, message = JsonErrorMsg(
//                code = HttpStatusCode.NotFound.value.toString(),
//                message = "Character not found"
//        ))
//    }
//
//    exception<NotSupportedLanguageException> { e ->
//        call.respond(HttpStatusCode.BadRequest, message = JsonErrorMsg(
//                code = HttpStatusCode.BadRequest.value.toString(),
//                message = e.message.orEmpty()
//        ))
//    }

    exception<NumberFormatException> { e ->
        call.respond(HttpStatusCode.BadRequest, message = ErrorMsgModule(
                code = HttpStatusCode.BadRequest.value.toString(),
                message = e.message.orEmpty()
        ))
    }

    exception<IllegalStateException> { e ->
        call.respond(HttpStatusCode.BadRequest, message = ErrorMsgModule(
                code = HttpStatusCode.BadRequest.value.toString(),
                message = e.message.orEmpty()
        ))
    }

    exception<Throwable> { e ->
        call.respond(HttpStatusCode.InternalServerError, message = ErrorMsgModule(
                code = HttpStatusCode.InternalServerError.value.toString(),
                message = e.message.orEmpty()
        ))
    }
}