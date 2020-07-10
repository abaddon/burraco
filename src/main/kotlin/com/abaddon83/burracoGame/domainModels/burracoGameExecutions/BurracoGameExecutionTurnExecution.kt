package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class BurracoGameExecutionTurnExecution private constructor(
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile,
        override val identity: GameIdentity) : BurracoGameExecution() {

    override fun updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List<Card>): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        return this.copy(players = UpdatePlayers(player.orderPlayerCards(orderedCards)))
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
        assert(player.showMyCards().isEmpty()) { "The player cannot pick up a Mazzetto if he still has cards" }
        assert(!player.isMazzettoTaken()) { "The player cannot pick up a Mazzetto he already taken" }

        val mazzetto = mazzettoDecks.firstMazzettoAvailable()
        mazzettoDecks.mazzettoTaken(mazzetto)

        return copy(
                players = UpdatePlayers(player.pickUpMazzetto(mazzetto)),
                mazzettoDecks = mazzettoDecks.mazzettoTaken(mazzetto)
        )
    }

    fun dropCardOnDiscardPile(playerIdentity: PlayerIdentity, card: Card): BurracoGameExecutionTurnEnd {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return BurracoGameExecutionTurnEnd.create(
                players = UpdatePlayers(player.dropACard(card)),
                playerTurn = playerTurn,
                burracoDeck = burracoDeck,
                mazzettoDecks = mazzettoDecks,
                discardPile = discardPile.addCard(card),
                identity = identity()
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