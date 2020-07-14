package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

abstract class BurracoGameExecution : BurracoGame() {
    abstract override val players: List<PlayerInGame>
    protected abstract val playerTurn: PlayerIdentity
    protected abstract val burracoDeck: BurracoDeck
    protected abstract val mazzettoDecks: MazzettoDecks
    protected abstract val discardPile: DiscardPile

    //READ Methods
    fun playerCards(playerIdentity: PlayerIdentity): List<Card> = players.find { p -> p.identity() == playerIdentity }
            ?.let { player ->
                player.showMyCards()
            } ?: throw NoSuchElementException("Player $playerIdentity is not a player of this game ${identity()}")

    fun playerTrisOnTable(playerIdentity: PlayerIdentity): List<BurracoTris> = players.find { p -> p.identity() == playerIdentity }
            ?.let { player ->
                player.showTrisOnTable()
            } ?: throw NoSuchElementException("Player $playerIdentity is not a player of this game ${identity()}")

    fun playerScalesOnTable(playerIdentity: PlayerIdentity): List<BurracoScale> = players.find { p -> p.identity() == playerIdentity }
            ?.let { player ->
                player.showScalesOnTable()
            } ?: throw NoSuchElementException("Player ${playerIdentity} is not a player of this game ${identity()}")

    fun showDiscardPile(): List<Card> = discardPile.showCards()

    fun validatePlayerTurn(playerIdentity: PlayerIdentity): Unit =
            check(playerTurn != playerIdentity) {
                warnMsg("It's not the turn of the player $playerIdentity")
            }


    fun testInvariants() = invariantNumCardsInGame()

    // write
    abstract fun updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List<Card>): BurracoGameExecution


    protected fun UpdatePlayers(burracoPlayerInGame: PlayerInGame): List<PlayerInGame> = players.map { playerInGame ->
        if (playerInGame.identity() == burracoPlayerInGame.identity()) {
            burracoPlayerInGame
        } else
            playerInGame
    }

    //validation
    protected fun validatePlayerId(playerIdentity: PlayerIdentity): PlayerInGame =
            checkNotNull(players.find { p -> p.identity() == playerIdentity }) {
                errorMsg("Player $playerIdentity is not a player of this game ${identity()}")
            }

    private fun numCardsInGame(): Int {
        val playersCardsTot = players.map { player ->
            player.totalPlayerCards()
        }.fold(0) { total, item -> total + item }
        return playersCardsTot + burracoDeck.numCards() + mazzettoDecks.numCards() + discardPile.numCards()
    }

    private fun invariantNumCardsInGame(): Unit {
        assert(totalCardsRequired == numCardsInGame()) { "The cards in game are not ${totalCardsRequired}. Founds ${numCardsInGame()}" }
    }


}