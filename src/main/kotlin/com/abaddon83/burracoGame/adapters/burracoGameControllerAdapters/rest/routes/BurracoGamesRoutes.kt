package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.routes

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.requests.DropCardRequest
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.requests.JoinGameRequest
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.requests.PickUpCardRequest
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.requests.StartGameRequest
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.BurracoGameModule
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.CardModule
import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.CardsModule
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.util.*

fun Routing.apiBurracoGames(controller: BurracoGameControllerPort) {

    route("games/burraco/{gameId}") {
        get() {
            val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
            val burracoGame = controller.findBurracoGameBy(gameIdentity)
                    ?: throw NoSuchElementException("BurracoGame with $gameIdentity not found")

            call.respond(BurracoGameModule.from(burracoGame))
        }
        route("join") {
            post {
                val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                val joinGameRequest = requireNotNull(call.receive<JoinGameRequest>()) { "Request missing or malformed" }
                val game = controller.joinPlayer(burracoGameIdentity = gameIdentity, playerIdentity = PlayerIdentity(joinGameRequest.playerIdentity))
                call.respond(BurracoGameModule.from(game))
            }
        }
        route("start") {
            post {
                val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                val startGameRequest = requireNotNull(call.receive<StartGameRequest>()) { "Request missing or malformed" }
                val game = controller.startGame(burracoGameIdentity = gameIdentity, playerIdentity = PlayerIdentity(startGameRequest.playerIdentity))
                call.respond(BurracoGameModule.from(game))
            }
        }
        route("players") {
            route("{playerId}") {
                route("cards") {
                    get {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        val playerCards = controller.showPlayerCards(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity)
                        call.respond(CardsModule.from(playerCards))
                    }
                }
                route("pickupcard") {
                    post {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        val game = controller.pickUpCardFromDeck(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity)
                        call.respond(BurracoGameModule.from(game))
                    }
                }
                route("dropcard") {
                    post {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        val cardToDrop = requireNotNull(call.receive<DropCardRequest>()) { "Request missing or malformed" }.card.to()
                        val game = controller.dropCardOnDiscardPile(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity, cardToDrop = cardToDrop)
                        call.respond(BurracoGameModule.from(game))
                    }
                }
            }

        }
    }
}