package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded

data class BurracoGameModule(
        val identity: GameIdentityModule,
        val status: String,
        val players: List<BurracoPlayerModule>,
        val playerTurn: PlayerIdentityModule? = null,
        val numMazzettoAvailable: Int = 0,
        val discardPile: List<CardModule>? = listOf()


) {
    companion object Factory {
        fun from(game: BurracoGame): BurracoGameModule =
                when (game) {
                    is BurracoGameWaitingPlayers -> from(game as BurracoGameWaitingPlayers)
                    is BurracoGameExecution -> from(game as BurracoGameExecution)
                    is BurracoGameEnded -> from(game as BurracoGameEnded)
                    else -> throw ClassCastException("Burraco game type not found")
                }

        fun from(game: BurracoGameWaitingPlayers): BurracoGameModule {
            return BurracoGameModule(
                    identity = GameIdentityModule(game.identity()),
                    status = "WAITING_PLAYERS",
                    players = game.listOfPlayers().map { BurracoPlayerModule.from(it) }
            )
        }

        fun from(game: BurracoGameExecution): BurracoGameModule {
            return BurracoGameModule(
                    identity = GameIdentityModule(game.identity()),
                    status = "EXECUTION",
                    players = game.listOfPlayers().map { BurracoPlayerModule.from(it) },
                    playerTurn = PlayerIdentityModule.from(game.showPlayerTurn()),
                    numMazzettoAvailable = game.showNumMazzettoAvailable(),
                    discardPile = game.showDiscardPile().map { CardModule.from(it) }
            )
        }

        fun from(game: BurracoGameEnded): BurracoGameModule {
            return BurracoGameModule(
                    identity = GameIdentityModule(game.identity()),
                    status = "ENDED",
                    players = game.listOfPlayers().map { BurracoPlayerModule.from(it) }
            )
        }
        
    }

}