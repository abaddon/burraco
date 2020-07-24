package com.abaddon83.burracoGame.api.routes

import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort
import com.abaddon83.burracoGame.api.messages.requests.CreateGameRequest
import com.abaddon83.burracoGame.api.messages.requests.GameType
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route

fun Routing.apiGames(commandController: BurracoGameCommandControllerPort, readModelController: BurracoGameReadModelControllerPort) {
    route("games") {
        post {
            val request = call.receive<CreateGameRequest>()
            val gameIdentity = GameIdentity.create()
            when (request.gameType) {
                GameType.BURRACO -> commandController.createNewBurracoGame(gameIdentity)
            }
            //TODO query to add
            call.respond(gameIdentity)
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