package com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions

import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.events.TurnEnded
import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.DiscardPile
import com.abaddon83.burracoGame.writeModel.models.MazzettoDecks
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.es.UnsupportedEventException

data class BurracoGameExecutionTurnEnd private constructor(
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile,
        override val identity: GameIdentity
) : BurracoGameExecution(identity) {


    override fun applyEvent(event: Event): BurracoGame {
        log.info("apply event: ${event::class.simpleName.toString()}")
        return when (event) {
            is TurnEnded -> apply(event)
            else -> throw UnsupportedEventException(event::class.java)
        }
    }


    private fun apply(event: TurnEnded): BurracoGameExecutionTurnBeginning {
        return BurracoGameExecutionTurnBeginning.create(
                identity = identity(),
                players = players,
                burracoDeck = burracoDeck,
                mazzettoDecks = mazzettoDecks,
                discardPile = discardPile,
                playerTurn = event.nextPlayerTurn
        )
    }


    fun pickupMazzetto(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnEnd {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        check(player.showMyCards().isEmpty()) { warnMsg("The player cannot pick up a Mazzetto if he still has cards") }
        check(!player.isMazzettoTaken()) { warnMsg("The player cannot pick up a Mazzetto he already taken") }

        val mazzetto = mazzettoDecks.firstMazzettoAvailable()

        return copy(
                players = UpdatePlayers(player.pickUpMazzetto(mazzetto)),
                mazzettoDecks = mazzettoDecks.mazzettoTaken(mazzetto)
        )
    }

    fun nextPlayerTurn(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnBeginning {
        validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        val list = players.map { it.identity() }
        val nextPlayerTurn = list[(list.indexOf(playerTurn) + 1) % list.size]

        return applyAndQueueEvent(
                TurnEnded(identity = identity(), playerIdentity = playerIdentity, nextPlayerTurn = nextPlayerTurn)
        )
    }

    fun completeGame(playerIdentity: PlayerIdentity): BurracoGameEnded {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        check(player.showMyCards().isEmpty()) { warnMsg("The player cannot complete the game with ${player.showMyCards().size} cards on hand") }
        check(player.isMazzettoTaken()) { warnMsg("The player cannot complete the game if the small deck is not taken (status: ${player.isMazzettoTaken()})") }
        check(player.burracoList().isNotEmpty()) { warnMsg("The player doesn't have a burraco") }
        //TODO add the logic to check if the squad taken the pozzetto

        return BurracoGameEnded.create(identity(), players, mazzettoDecks, playerTurn)
    }

    override fun listOfPlayers(): List<PlayerInGame> = this.players

    companion object Factory {
        fun create(players: List<PlayerInGame>, playerTurn: PlayerIdentity, burracoDeck: BurracoDeck, mazzettoDecks: MazzettoDecks, discardPile: DiscardPile, identity: GameIdentity): BurracoGameExecutionTurnEnd {
            val game = BurracoGameExecutionTurnEnd(
                    players = players,
                    playerTurn = playerTurn,
                    burracoDeck = burracoDeck,
                    mazzettoDecks = mazzettoDecks,
                    discardPile = discardPile,
                    identity = identity)
            game.testInvariants()
            return game
        }
    }
}


//Events


