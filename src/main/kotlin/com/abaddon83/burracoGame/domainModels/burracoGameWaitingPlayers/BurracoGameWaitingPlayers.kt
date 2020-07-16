package com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.es.AggregateRoot
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException
import java.time.LocalDate

data class BurracoGameWaitingPlayers constructor(
        override val identity: GameIdentity,
        override val players: List<BurracoPlayer>) : BurracoGame(identity) {

    fun addPlayer(player: BurracoPlayer): BurracoGameWaitingPlayers {
        check(players.size < maxPlayers) {
            warnMsg("Maximum number of players reached, (Max: ${maxPlayers})")
        }
        check(!isAlreadyAPlayer(player.identity())) {
            warnMsg("The player ${player.identity()} is already a player of game ${this.identity()}")
        }
        return applyAndQueueEvent(PlayerAdded(gameIdentity = identity, burracoPlayer = player))//BurracoGameWaitingPlayers(identity, listOf(players, listOf(player)).flatten())
    }

    fun isAlreadyAPlayer(playerIdentity: PlayerIdentity): Boolean {
        return players.find { p -> p.identity() == playerIdentity } != null
    }

    fun start(): BurracoGameExecutionTurnBeginning {
        check(players.size > 1) {
            warnMsg("Not enough players to initiate the game, ( Min: ${minPlayers})")
        }
        val burracoDealer = BurracoDealer.create(this)
        return applyAndQueueEvent(GameStarted(
                gameIdentity = identity(),
                playersCards = burracoDealer.playersCards,
                burracoDeckCards = burracoDealer.burracoDeck.cards.toList(),
                mazzettoDeck1Cards = burracoDealer.mazzettoDecks.list.first().cards.toList(),
                mazzettoDeck2Cards = burracoDealer.mazzettoDecks.list.last().cards.toList(),
                discardPileCards = burracoDealer.discardPile.showCards(),
                playerTurn = players[0].identity())
        )
    }

    override fun applyEvent(event: Event): BurracoGame =
            when (event) {
                is GameStarted -> apply(event)
                is PlayerAdded -> apply(event)
                else -> throw UnsupportedEventException(event::class.java)
            }

    private fun apply(event: PlayerAdded): BurracoGameWaitingPlayers {
        return copy(players = players.plus(event.burracoPlayer))
    }

    private fun apply(event: GameStarted): BurracoGameExecutionTurnBeginning {
        val burracoPlayersInGame = event.playersCards.map { (playerIdentity, playerCards) ->
            PlayerInGame.create(playerIdentity = playerIdentity, cards = playerCards)
        }
        val gameUpdated = BurracoGameExecutionTurnBeginning.create(
                identity = identity(),
                players = burracoPlayersInGame,
                burracoDeck = BurracoDeck.create(event.burracoDeckCards),
                mazzettoDecks = MazzettoDecks.create(listOf(
                        MazzettoDeck.create(event.mazzettoDeck1Cards),
                        MazzettoDeck.create(event.mazzettoDeck2Cards))),
                discardPile = DiscardPile.create(event.discardPileCards),
                playerTurn = event.playerTurn
        )
        gameUpdated.testInvariants()
        return gameUpdated
    }

}

//Events

data class PlayerAdded(
        val gameIdentity: GameIdentity,
        val burracoPlayer: BurracoPlayer,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): PlayerAdded =
            this.copy(version = version)
}

data class GameStarted(
        val gameIdentity: GameIdentity,
        val playersCards: Map<PlayerIdentity, List<Card>>,
        val burracoDeckCards: List<Card>,
        val mazzettoDeck1Cards: List<Card>,
        val mazzettoDeck2Cards: List<Card>,
        val discardPileCards: List<Card>,
        val playerTurn: PlayerIdentity,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): GameStarted =
            this.copy(version = version)
}