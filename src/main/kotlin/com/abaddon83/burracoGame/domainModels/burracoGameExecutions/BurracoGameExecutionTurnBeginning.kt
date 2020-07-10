package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.BurracoDeck
import com.abaddon83.burracoGame.domainModels.DiscardPile
import com.abaddon83.burracoGame.domainModels.MazzettoDecks
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoCardsDealt
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import java.lang.Exception

data class BurracoGameExecutionTurnBeginning private constructor(
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile,
        override val identity: GameIdentity
) : BurracoGameExecution() {


    //all players can order cards when they want to
    override fun updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List<Card>): BurracoGameExecution {
        val player = validatePlayerId(playerIdentity)
        return this.copy(players = UpdatePlayers(player.orderPlayerCards(orderedCards)))
    }

    //When the turn start the player can pickUp a card from the Deck
    fun pickUpACardFromDeck(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)

        return BurracoGameExecutionTurnExecution.create(
                this.identity(),
                UpdatePlayers(player.addCardsOnMyCard(listOf(burracoDeck.grabFirstCard()))),
                this.burracoDeck,
                this.mazzettoDecks,
                this.discardPile,
                this.playerTurn
        )
    }

    //When the turn start the player can pickUp all cards from the DiscardPile if it's not empty
    fun pickUpCardsFromDiscardPile(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)

        return BurracoGameExecutionTurnExecution.create(
                this.identity(),
                UpdatePlayers(player.addCardsOnMyCard(discardPile.grabAllCards())),
                this.burracoDeck,
                this.mazzettoDecks,
                this.discardPile,
                this.playerTurn
        )
    }


    companion object Factory {
        fun create(identity: GameIdentity, players: List<PlayerInGame>, burracoDeck: BurracoDeck, mazzettoDecks: MazzettoDecks, discardPile: DiscardPile, playerTurn: PlayerIdentity): BurracoGameExecutionTurnBeginning {

        val game =BurracoGameExecutionTurnBeginning(
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

        fun create(burracoGameWaitingPlayers: BurracoGameWaitingPlayers, burracoCardsDealt: BurracoCardsDealt): BurracoGameExecutionTurnBeginning {
            assert(burracoGameWaitingPlayers.listOfPlayers().map { player -> player.identity() }.containsAll(burracoCardsDealt.playersCards.keys)) { "One or more players doesn't have their cards" }

            val burracoPlayersInGame = burracoGameWaitingPlayers.listOfPlayers().map { player ->
                PlayerInGame.create(
                        player.identity(),
                        burracoCardsDealt.playersCards[player.identity()]?.let { list -> list } ?: throw Exception("")
                )
            }

            val game =BurracoGameExecutionTurnBeginning(
                    identity = burracoGameWaitingPlayers.identity(),
                    players = burracoPlayersInGame,
                    burracoDeck = burracoCardsDealt.burracoDeck,
                    mazzettoDecks = burracoCardsDealt.mazzettoDecks,
                    discardPile = burracoCardsDealt.discardPile,
                    playerTurn = burracoPlayersInGame[0].identity
            )
            game.testInvariants()
            return game
        }
    }

}