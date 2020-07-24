package com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.commandModel.models.*
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.Ranks
import com.abaddon83.burracoGame.commandModel.models.decks.Suits
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException

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
        return applyAndQueueEvent(PlayerAdded.create(gameIdentity = identity, playerIdentity = player.identity()))
    }

    fun isAlreadyAPlayer(playerIdentity: PlayerIdentity): Boolean {
        return players.find { p -> p.identity() == playerIdentity } != null
    }

    fun start(): BurracoGameExecutionTurnBeginning {
        check(players.size > 1) {
            warnMsg("Not enough players to initiate the game, ( Min: ${minPlayers})")
        }
        val burracoDealer = BurracoDealer.create(this)
        return applyAndQueueEvent(GameStarted.create(
                gameIdentity = identity(),
                playersCards = burracoDealer.playersCards,
                burracoDeckCards = burracoDealer.burracoDeck.cards.toList(),
                mazzettoDeck1Cards = burracoDealer.mazzettoDecks.list.first().cards.toList(),
                mazzettoDeck2Cards = burracoDealer.mazzettoDecks.list.last().cards.toList(),
                discardPileCards = burracoDealer.discardPile.showCards(),
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
        return copy(players = players.plus(PlayerInGame.create(PlayerIdentity.create(event.playerIdentity), listOf())))
    }

    private fun apply(event: GameStarted): BurracoGameExecutionTurnBeginning {
        val burracoPlayersInGame = event.players.map { list ->
            PlayerInGame.create(
                    playerIdentity = PlayerIdentity.create(list.identity),
                    cards = list.cards.map { c -> Card(Suits.valueOf(c.suit), Ranks.valueOf(c.rank)) }
            )
        }
        val gameUpdated = BurracoGameExecutionTurnBeginning.create(
                identity = identity(),
                players = burracoPlayersInGame,
                burracoDeck = BurracoDeck.create(event.burracoDeckCards.map { c -> Card(Suits.valueOf(c.suit), Ranks.valueOf(c.rank)) }),
                mazzettoDecks = MazzettoDecks.create(listOf(
                        MazzettoDeck.create(event.mazzettoDeck1Cards.map { c -> Card(Suits.valueOf(c.suit), Ranks.valueOf(c.rank)) }),
                        MazzettoDeck.create(event.mazzettoDeck2Cards.map { c -> Card(Suits.valueOf(c.suit), Ranks.valueOf(c.rank)) }))),
                discardPile = DiscardPile.create(event.discardPileCards.map { c -> Card(Suits.valueOf(c.suit), Ranks.valueOf(c.rank)) }),
                playerTurn = PlayerIdentity.create(event.playerIdentityTurn)
        )
        gameUpdated.testInvariants()
        return gameUpdated
    }

}

//Events

data class PlayerAdded(
        val gameIdentity: String,
        val playerIdentity: String,
        val version: Long? = null) : Event(version) {
    override fun assignVersion(version: Long): PlayerAdded =
            this.copy(version = version)

    companion object Factory {
        fun create(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): PlayerAdded =
                PlayerAdded(gameIdentity = gameIdentity.convertTo().toString(), playerIdentity = playerIdentity.convertTo().toString())
    }
}

data class GameStarted(
        val gameIdentity: String,
        val players: List<Player>,
        val burracoDeckCards: List<BasicCard>,
        val mazzettoDeck1Cards: List<BasicCard>,
        val mazzettoDeck2Cards: List<BasicCard>,
        val discardPileCards: List<BasicCard>,
        val playerIdentityTurn: String,
        val version: Long? = null) : Event(version) {
    override fun assignVersion(version: Long): GameStarted =
            this.copy(version = version)

    data class BasicCard(val suit: String, val rank: String)
    data class Player(val identity: String, val cards: List<BasicCard>)

    companion object Factory {
        fun create(gameIdentity: GameIdentity,
                   playersCards: Map<PlayerIdentity, List<Card>>,
                   burracoDeckCards: List<Card>,
                   mazzettoDeck1Cards: List<Card>,
                   mazzettoDeck2Cards: List<Card>,
                   discardPileCards: List<Card>,
                   playerTurn: PlayerIdentity): GameStarted =
                GameStarted(
                        gameIdentity = gameIdentity.convertTo().toString(),
                        players = playersCards.map { (playerIdentity, cards) -> Player(playerIdentity.convertTo().toString(), cards.map { card -> BasicCard(card.suit.javaClass.simpleName, card.rank.javaClass.simpleName) }) },
                        burracoDeckCards = burracoDeckCards.map { card -> BasicCard(card.suit.javaClass.simpleName, card.rank.javaClass.simpleName) },
                        mazzettoDeck1Cards = mazzettoDeck1Cards.map { card -> BasicCard(card.suit.javaClass.simpleName, card.rank.javaClass.simpleName) },
                        mazzettoDeck2Cards = mazzettoDeck2Cards.map { card -> BasicCard(card.suit.javaClass.simpleName, card.rank.javaClass.simpleName) },
                        discardPileCards = discardPileCards.map { card -> BasicCard(card.suit.javaClass.simpleName, card.rank.javaClass.simpleName) },
                        playerIdentityTurn = playerTurn.convertTo().toString()
                )
    }
}