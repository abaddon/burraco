package com.abaddon83.burracoGame.writeModel.models.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.events.GameStarted
import com.abaddon83.burracoGame.writeModel.events.PlayerAdded
import com.abaddon83.burracoGame.writeModel.models.*
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.es.UnsupportedEventException


data class BurracoGameWaitingPlayers constructor(
        override val identity: GameIdentity,
        override val players: List<BurracoPlayer>,
        val burracoDeck: BurracoDeck) : BurracoGame(identity) {

    fun addPlayer(player: BurracoPlayer): BurracoGameWaitingPlayers {
        check(players.size < maxPlayers) {
            warnMsg("Maximum number of players reached, (Max: ${maxPlayers})")
        }
        check(!isAlreadyAPlayer(player.identity())) {
            warnMsg("The player ${player.identity()} is already a player of game ${this.identity()}")
        }
        return applyAndQueueEvent(PlayerAdded(identity = identity, playerIdentity = player.identity()))
    }

    fun isAlreadyAPlayer(playerIdentity: PlayerIdentity): Boolean {
        return players.find { p -> p.identity() == playerIdentity } != null
    }

    fun start(): BurracoGameExecutionTurnBeginning {
        check(players.size > 1) {
            warnMsg("Not enough players to initiate the game, ( Min: ${minPlayers})")
        }
        val burracoDealer = BurracoDealer.create(deck, players)
        return applyAndQueueEvent(GameStarted(
                identity = identity(),
                deck = burracoDealer.burracoDeck.cards,
                players = burracoDealer.dealCardsToPlayers,
                mazzettoDeck1 = burracoDealer.dealCardsToFirstMazzetto.getCardList(),
                mazzettoDeck2 = burracoDealer.dealCardsToSecondMazzetto.getCardList(),
                discardPileCards = burracoDealer.dealCardToDiscardPile.showCards(),
                playerTurn = players[0].identity())
        )
    }

    override fun applyEvent(event: Event): BurracoGame {
        log.info("apply event: ${event::class.simpleName.toString()}")
        return when (event) {
            is GameStarted -> apply(event)
            is PlayerAdded -> apply(event)
            else -> throw UnsupportedEventException(event::class.java)
        }
    }


    private fun apply(event: PlayerAdded): BurracoGameWaitingPlayers {
        return copy(players = players.plus(PlayerInGame.create(event.playerIdentity, listOf())))
    }

    private fun apply(event: GameStarted): BurracoGameExecutionTurnBeginning {
        val burracoPlayersInGame = event.players.map { (player, cards) ->
            PlayerInGame.create(
                    playerIdentity = player,
                    cards = cards
            )
        }
        val gameUpdated = BurracoGameExecutionTurnBeginning.create(
                identity = identity(),
                players = burracoPlayersInGame,
                burracoDeck = BurracoDeck.create(event.deck),
                mazzettoDecks = MazzettoDecks.create(listOf(
                        MazzettoDeck.create(event.mazzettoDeck1),
                        MazzettoDeck.create(event.mazzettoDeck2)
                )),
                discardPile = DiscardPile.create(event.discardPileCards),
                playerTurn = event.playerTurn
        )
        gameUpdated.testInvariants()
        return gameUpdated
    }

}
