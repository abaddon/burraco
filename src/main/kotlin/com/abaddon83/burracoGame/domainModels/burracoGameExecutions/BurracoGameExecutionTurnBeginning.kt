package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoCardsDealt
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.GameStarted
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException
import java.lang.Exception

data class BurracoGameExecutionTurnBeginning private constructor(
        override val identity: GameIdentity,
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile
) : BurracoGameExecution(identity) {

    companion object Factory {
        fun create(identity: GameIdentity, players: List<PlayerInGame>, burracoDeck: BurracoDeck, mazzettoDecks: MazzettoDecks, discardPile: DiscardPile, playerTurn: PlayerIdentity): BurracoGameExecutionTurnBeginning {
            val game = BurracoGameExecutionTurnBeginning(
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

    //When the turn start the player can pickUp a card from the Deck
    fun pickUpACardFromDeck(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)

        return applyAndQueueEvent(
                CardPickedFromDeck(this.identity, player.identity(), burracoDeck.getFirstCard())
        )
    }

    //When the turn start the player can pickUp all cards from the DiscardPile if it's not empty
    fun pickUpCardsFromDiscardPile(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)

        return applyAndQueueEvent(
                CardsPickedFromDiscardPile(this.identity, player.identity(), discardPile.grabAllCards())
        )
    }

    override fun applyEvent(event: Event): BurracoGame =
            when (event) {
                is CardPickedFromDeck -> apply(event)
                is CardsPickedFromDiscardPile -> apply(event)
                else -> throw UnsupportedEventException(event::class.java)
            }

    private fun apply(event: CardPickedFromDeck): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.player }!!
        val deckCards = burracoDeck.cards.toList().minusElement(event.cardTaken)
        return BurracoGameExecutionTurnExecution.create(
                identity = this.identity(),
                players = UpdatePlayers(player.addCardsOnMyCard(listOf(event.cardTaken))),
                burracoDeck = BurracoDeck.create(this.burracoDeck.cards.toList().minusElement(event.cardTaken)),
                mazzettoDecks = this.mazzettoDecks,
                discardPile = this.discardPile,
                playerTurn = this.playerTurn)
    }

    private fun apply(event: CardsPickedFromDiscardPile): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.player }!!
        val discardPileCards = listOf<Card>()
        return BurracoGameExecutionTurnExecution.create(
                identity = this.identity(),
                players = UpdatePlayers(player.addCardsOnMyCard(event.cardsTaken)),
                burracoDeck = this.burracoDeck,
                mazzettoDecks = this.mazzettoDecks,
                discardPile = DiscardPile.create(discardPileCards),
                playerTurn = this.playerTurn
        )
    }

}

//Events
data class CardPickedFromDeck(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val cardTaken: Card,
        val version: Long? = null
) : Event(version) {
    override fun assignVersion(version: Long): CardPickedFromDeck =
            this.copy(version = version)
}

data class CardsPickedFromDiscardPile(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val cardsTaken: List<Card>,
        val version: Long? = null
) : Event(version) {
    override fun assignVersion(version: Long): CardsPickedFromDiscardPile =
            this.copy(version = version)
}
