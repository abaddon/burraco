package com.abaddon83.burracoGame.api.messages

import com.abaddon83.burracoGame.readModel.models.BurracoGame
import java.util.*

data class BurracoGameModule(
        val identity: UUID,
        val status: String,
        val players: List<BurracoPlayerModule>,
        val playerTurn: BurracoPlayerModule? = null,
        val numMazzettoAvailable: Int = 0//,
        //val discardPile: List<CardModule>? = listOf()


) {
    companion object Factory {
        fun from(game: BurracoGame): BurracoGameModule =
                BurracoGameModule(
                        identity = game.identity,
                        status = game.status,
                        players = game.players.map { BurracoPlayerModule.from(it) },
                        playerTurn = game.playerTurn?.let { BurracoPlayerModule.from(game.playerTurn) } ?: null,
                        numMazzettoAvailable = game.numMazzettoAvailable
                        //discardPile = game.discardPile
                )

//        fun from(game: BurracoGameWaitingPlayers): BurracoGameModule {
//            return BurracoGameModule(
//                    identity = GameIdentityModule(game.identity()),
//                    status = "WAITING_PLAYERS",
//                    players = game.listOfPlayers().map { BurracoPlayerModule.from(it) }
//            )
//        }
//
//        fun from(game: BurracoGameExecution): BurracoGameModule {
//            return BurracoGameModule(
//                    identity = GameIdentityModule(game.identity()),
//                    status = "EXECUTION",
//                    players = game.listOfPlayers().map { BurracoPlayerModule.from(it) },
//                    playerTurn = PlayerIdentityModule.from(game.showPlayerTurn()),
//                    numMazzettoAvailable = game.showNumMazzettoAvailable(),
//                    discardPile = game.showDiscardPile().map { CardModule.from(it) }
//            )
//        }
//
//        fun from(game: BurracoGameEnded): BurracoGameModule {
//            return BurracoGameModule(
//                    identity = GameIdentityModule(game.identity()),
//                    status = "ENDED",
//                    players = game.listOfPlayers().map { BurracoPlayerModule.from(it) }
//            )
//        }
        
    }

}