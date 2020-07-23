package com.abaddon83.burracoGame.api.routes

import com.abaddon83.burracoGame.api.messages.requests.DropCardRequest
import com.abaddon83.burracoGame.api.messages.requests.JoinGameRequest
import com.abaddon83.burracoGame.api.messages.requests.StartGameRequest
import com.abaddon83.burracoGame.api.messages.BurracoGameModule
import com.abaddon83.burracoGame.api.messages.CardsModule
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.util.*

fun Routing.apiBurracoGames(commandController: BurracoGameCommandControllerPort, readModelController: BurracoGameReadModelControllerPort) {

    route("games/burraco/{gameId}") {
//        get() {
//            val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
//            val burracoGame = readModelController.findBurracoGameBy(gameIdentity)
//                    ?: throw NoSuchElementException("BurracoGame with $gameIdentity not found")
//
//            call.respond(BurracoGameModule.from(burracoGame))
//        }
        route("join") {
            post {
                val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                val joinGameRequest = requireNotNull(call.receive<JoinGameRequest>()) { "Request missing or malformed" }
                commandController.joinPlayer(burracoGameIdentity = gameIdentity, playerIdentity = PlayerIdentity(joinGameRequest.playerIdentity))
                call.respond("TMP OK")//BurracoGameModule.from(game))
            }
        }
        route("start") {
            post {
                val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                val startGameRequest = requireNotNull(call.receive<StartGameRequest>()) { "Request missing or malformed" }
                commandController.startGame(burracoGameIdentity = gameIdentity, playerIdentity = PlayerIdentity(startGameRequest.playerIdentity))
                call.respond("TMP OK")//BurracoGameModule.from(game))
            }
        }
        route("players") {
            route("{playerId}") {
//                route("cards") {
//                    get {
//                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
//                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
//                        val playerCards = readModelController.showPlayerCards(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity)
//                        call.respond(CardsModule.from(playerCards))
//                    }
//                }
                route("pickupcard") {
                    post {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        commandController.pickUpCardFromDeck(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity)
                        call.respond("TMP OK")//BurracoGameModule.from(game))
                    }
                }
                route("dropcard") {
                    post {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        val cardToDrop = requireNotNull(call.receive<DropCardRequest>()) { "Request missing or malformed" }.card.to()
                        commandController.dropCardOnDiscardPile(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity, cardToDrop = cardToDrop)
                        call.respond("TMP OK")//BurracoGameModule.from(game))
                    }
                }
            }

        }
    }
}