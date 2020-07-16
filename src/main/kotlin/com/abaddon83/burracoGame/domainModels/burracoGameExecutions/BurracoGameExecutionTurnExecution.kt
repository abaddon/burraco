package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException

data class BurracoGameExecutionTurnExecution private constructor(
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile,
        override val identity: GameIdentity) : BurracoGameExecution(identity) {

    override fun applyEvent(event: Event): BurracoGame =
            when (event) {
                is CardDroppedIntoDiscardPile -> apply(event)
                else -> throw UnsupportedEventException(event::class.java)
            }

    private fun apply(event: CardDroppedIntoDiscardPile): BurracoGameExecutionTurnEnd {
        val player = players.find { p -> p.identity() == event.player }!!
        val deckCards = discardPile.cards.toList().plus(event.cardDropped)

        return BurracoGameExecutionTurnEnd.create(
                identity = identity(),
                players = UpdatePlayers(player.dropACard(event.cardDropped)),
                playerTurn = playerTurn,
                burracoDeck = burracoDeck,
                mazzettoDecks = mazzettoDecks,
                discardPile = discardPile.addCard(event.cardDropped)
        )
    }

    fun dropOnTableATris(playerIdentity: PlayerIdentity, tris: BurracoTris): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return copy(players = UpdatePlayers(player.dropATris(tris)))
    }

    fun dropOnTableAScale(playerIdentity: PlayerIdentity, scale: BurracoScale): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return copy(players = UpdatePlayers(player.dropAScale(scale)))
    }

    fun appendCardsOnABurracoDropped(playerIdentity: PlayerIdentity, cardsToAppend: List<Card>, burracoIdentity: BurracoIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return copy(players = UpdatePlayers(player.appendACardOnBurracoDropped(burracoIdentity, cardsToAppend)))
    }

    fun pickupMazzetto(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
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

    fun dropCardOnDiscardPile(playerIdentity: PlayerIdentity, card: Card): BurracoGameExecutionTurnEnd {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return applyAndQueueEvent(
                CardDroppedIntoDiscardPile(gameIdentity = identity(),player = player.identity(),cardDropped = card)
        )
    }

    companion object Factory {
        fun create(identity: GameIdentity, players: List<PlayerInGame>, burracoDeck: BurracoDeck, mazzettoDecks: MazzettoDecks, discardPile: DiscardPile, playerTurn: PlayerIdentity): BurracoGameExecutionTurnExecution {
            val game = BurracoGameExecutionTurnExecution(
                    identity = identity,
                    players = players,
                    burracoDeck = burracoDeck,
                    mazzettoDecks = mazzettoDecks,
                    discardPile = discardPile,
                    playerTurn = playerTurn
            )
            game.testInvariants()
            return game
        }
    }
}

//Events
data class CardDroppedIntoDiscardPile(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val cardDropped: Card,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): CardDroppedIntoDiscardPile =
            this.copy(version = version)
}