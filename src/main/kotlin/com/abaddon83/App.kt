/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.abaddon83

import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort
import com.abaddon83.burracoGame.api.handleExceptions.errorsHandling
import com.abaddon83.burracoGame.api.routes.apiBurracoGames
import com.abaddon83.burracoGame.api.routes.apiGames
import com.abaddon83.burracoGame.iocs.AppAdapters
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

//class App {
//    val greeting: String
//        get() {
//            return "Hello world."
//        }
//}
//
//fun main(args: Array<String>) {
//    println(App().greeting)
//}

fun Application.di() {
    //DI setup
    install(Koin) {
        printLogger()
        modules(AppAdapters)
    }
}

fun Application.main() {
    val burracoGameCommandController: BurracoGameCommandControllerPort by inject()
    val burracoGameReadModelController: BurracoGameReadModelControllerPort by inject()

    //HTTP
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            register(ContentType.Application.Json, JacksonConverter())

        }
    }

    install(StatusPages) {
        errorsHandling()
    }
    install(Routing) {
        apiGames(burracoGameCommandController,burracoGameReadModelController)
        apiBurracoGames(burracoGameCommandController,burracoGameReadModelController)
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}
