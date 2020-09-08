package com.abaddon83.burracoGame.readModel.adapters.readModelRestAdapter

import com.abaddon83.burracoGame.readModel.ports.ReadModelControllerPort
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*
import kotlin.NoSuchElementException

fun Routing.queryApiBurracoGames(controller: ReadModelControllerPort) {

    route("games/burraco/{gameId}") {
        get() {
            val gameIdentity = requireNotNull(UUID.fromString(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
            val burracoGame = controller.findBurracoGame(gameIdentity)
                    ?: throw NoSuchElementException("BurracoGame with $gameIdentity not found")
            call.respond(burracoGame)
        }
    }

}