package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters

import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages.ErrorMsgModule
import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.bodyRequests.*
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.writeModel.ports.WriteModelControllerPort
import com.abaddon83.utils.functionals.Invalid
import com.abaddon83.utils.functionals.Valid
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.commandApiBurracoGames(controller: WriteModelControllerPort) {
    route("games/burraco") {
        post {
            val request = call.receive<CreateGameRequest>()
            val gameIdentity = GameIdentity.create()
            val outcome = when (request.gameType) {
                GameType.BURRACO -> controller.createNewBurracoGame(gameIdentity)
            }
            call.respond(outcome)
        }
        route("{gameId}") {
            route("join") {
                post {
                    val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                    val joinGameRequest = requireNotNull(call.receive<JoinGameRequest>()) { "Request missing or malformed" }
                    val outcome = controller.joinPlayer(burracoGameIdentity = gameIdentity, playerIdentity = PlayerIdentity(joinGameRequest.playerIdentity))
                    call.respond(when(outcome){
                        is Valid -> outcome
                        is Invalid -> ErrorMsgModule(code = HttpStatusCode.Forbidden.toString(), message = outcome.err.msg)
                    })
                }
            }
            route("start") {
                post {
                    val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                    val startGameRequest = requireNotNull(call.receive<StartGameRequest>()) { "Request missing or malformed" }
                    val outcome = controller.startGame(burracoGameIdentity = gameIdentity, playerIdentity = PlayerIdentity(startGameRequest.playerIdentity))
                    call.respond(when(outcome){
                        is Valid -> outcome
                        is Invalid -> ErrorMsgModule(code = HttpStatusCode.Forbidden.toString(), message = outcome.err.msg)
                    })
                }
            }
            route("players/{playerId}") {
                route("pickupcard") {
                    post {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        val outcome = controller.pickUpCardFromDeck(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity)
                        call.respond(when(outcome){
                            is Valid -> outcome
                            is Invalid -> ErrorMsgModule(code = HttpStatusCode.Forbidden.toString(), message = outcome.err.msg)
                        })
                    }
                }
                route("dropcard") {
                    post {
                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
                        val cardToDrop = requireNotNull(call.receive<DropCardRequest>()) { "Request missing or malformed" }.card.to()
                        val outcome = controller.dropCardOnDiscardPile(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity, cardToDrop = cardToDrop)
                        call.respond(when(outcome){
                            is Valid -> outcome
                            is Invalid -> ErrorMsgModule(code = HttpStatusCode.Forbidden.toString(), message = outcome.err.msg)
                        })
                    }
                }
            }
        }
    }
}