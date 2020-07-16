package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.BurracoDeck
import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.DiscardPile
import com.abaddon83.burracoGame.domainModels.MazzettoDecks
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException

data class BurracoGameExecutionTurnEnd private constructor(
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile,
        override val identity: GameIdentity
) : BurracoGameExecution(identity) {


    override fun applyEvent(event: Event): BurracoGame =
            when (event) {
                is TurnEnded -> apply(event)
                else -> throw UnsupportedEventException(event::class.java)
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
                TurnEnded(gameIdentity = identity(), player = playerIdentity, nextPlayerTurn = nextPlayerTurn)
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
data class TurnEnded(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val nextPlayerTurn: PlayerIdentity,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): TurnEnded =
            this.copy(version = version)
}

