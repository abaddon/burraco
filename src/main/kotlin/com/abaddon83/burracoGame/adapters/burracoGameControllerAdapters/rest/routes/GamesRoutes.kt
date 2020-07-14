package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.routes

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.requests.CreateGameRequest
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.requests.GameType
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses.GameResponse
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import java.lang.IllegalArgumentException

fun Routing.apiGames(controller: BurracoGameControllerPort) {
    route("games") {
        post {
            val request = call.receive<CreateGameRequest>()
            val gameResponse: GameResponse = when (request.gameType) {
                GameType.BURRACO -> GameResponse(controller.createNewBurracoGame())
                //else -> throw IllegalArgumentException("Game type ${request.gameType} not found")
            }
            call.respond(gameResponse)
        }
    }
}

//        get("/") {
//            call.respond(characterController.allCharacterIdentities().map{it.value}.toList())
//        }
//
//        get("/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
//            val targetLang = call.request.queryParameters["language"]
//            val character = {
//                if(targetLang.isNullOrEmpty())
//                    characterController.character(CharacterIdentity(id))
//                else
//                    characterController.characterLocalised(CharacterIdentity(id),targetLang)
//            }.invoke()
//            call.respond(JsonCharacter(character))
//        }