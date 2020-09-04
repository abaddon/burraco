package com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions

import com.abaddon83.burracoGame.writeModel.events.CardPickedFromDeck
import com.abaddon83.burracoGame.writeModel.events.CardsPickedFromDiscardPile
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.DiscardPile
import com.abaddon83.burracoGame.writeModel.models.MazzettoDecks
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.es.UnsupportedEventException

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
        return applyAndQueueEvent(CardPickedFromDeck(identity, player.identity(), burracoDeck.getFirstCard()))
    }

    //When the turn start the player can pickUp all cards from the DiscardPile if it's not empty
    fun pickUpCardsFromDiscardPile(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return applyAndQueueEvent(CardsPickedFromDiscardPile(identity, player.identity(), discardPile.grabAllCards()))
    }

    override fun applyEvent(event: Event): BurracoGame {
        log.info("apply event: ${event::class.simpleName.toString()}")
        return when (event) {
            is CardPickedFromDeck -> apply(event)
            is CardsPickedFromDiscardPile -> apply(event)
            else -> throw UnsupportedEventException(event::class.java)
        }
    }

    private fun apply(event: CardPickedFromDeck): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.playerIdentity }!!

        return BurracoGameExecutionTurnExecution.create(
                identity = this.identity(),
                players = UpdatePlayers(player.addCardsOnMyCard(listOf(event.card))),
                burracoDeck = BurracoDeck.create(this.burracoDeck.cards.minusElement(event.card)),
                mazzettoDecks = this.mazzettoDecks,
                discardPile = this.discardPile,
                playerTurn = this.playerTurn)
    }

    private fun apply(event: CardsPickedFromDiscardPile): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.playerIdentity }!!

        return BurracoGameExecutionTurnExecution.create(
                identity = this.identity(),
                players = UpdatePlayers(player.addCardsOnMyCard(event.cards)),
                burracoDeck = this.burracoDeck,
                mazzettoDecks = this.mazzettoDecks,
                discardPile = DiscardPile.create(discardPile.showCards().minus(event.cards)),
                playerTurn = this.playerTurn
        )
    }
}