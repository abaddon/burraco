package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.routes

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.requests.CreateGame
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses.GameResponse
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException

fun Routing.apiBurracoGames(controller: BurracoGameControllerPort) {

    route("games/burraco") {
        post {
            val request = call.receive<CreateGame>()
            val gameresponse: GameResponse = when(request.gameType){
                "burraco" -> GameResponse(controller.createNewBurracoGame())
                else -> throw IllegalArgumentException("Game type ${request.gameType} not found")
            }
            call.respond(gameresponse)
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
    }
}