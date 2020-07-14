package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.routes

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.requests.JoinGameRequest
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses.BurracoGameResponse
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.util.*

fun Routing.apiBurracoGames(controller: BurracoGameControllerPort) {

    route("games/burraco/{id}") {
        get() {
            val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["id"]!!)){ "GameIdentity missing" }
            val burracoGame = controller.findBurracoGameBy(gameIdentity)?: throw NoSuchElementException("BurracoGame with $gameIdentity not found")

            call.respond(BurracoGameResponse(burracoGame))
        }
        route("join"){
            post {
                val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["id"]!!)){ "GameIdentity missing" }
                val joinGameRequest = requireNotNull(call.receive<JoinGameRequest>()){ "Request missing or malformed" }
                val game = controller.joinPlayer(burracoGameIdentity = gameIdentity,playerIdentity = PlayerIdentity(joinGameRequest.playerIdentity))
                call.respond(BurracoGameResponse(game))
            }
        }
    }
}