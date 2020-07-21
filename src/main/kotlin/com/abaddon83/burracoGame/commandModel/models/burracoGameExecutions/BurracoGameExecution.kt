package com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions

import com.abaddon83.burracoGame.commandModel.models.*
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity

abstract class BurracoGameExecution(identity: GameIdentity) : BurracoGame(identity) {
    abstract override val players: List<PlayerInGame>
    protected abstract val playerTurn: PlayerIdentity
    protected abstract val burracoDeck: BurracoDeck
    protected abstract val mazzettoDecks: MazzettoDecks
    protected abstract val discardPile: DiscardPile

    //READ Methods
    fun playerCards(playerIdentity: PlayerIdentity): List<Card> =
            when (val player = players.find { p -> p.identity() == playerIdentity }) {
                is PlayerInGame -> player.showMyCards()
                else -> throw NoSuchElementException("Player $playerIdentity is not a player of this game ${identity()}")
            }

    fun playerTrisOnTable(playerIdentity: PlayerIdentity): List<BurracoTris> =
            when (val player = players.find { p -> p.identity() == playerIdentity }) {
                is PlayerInGame -> player.showTrisOnTable()
                else -> throw NoSuchElementException("Player $playerIdentity is not a player of this game ${identity()}")
            }

    fun playerScalesOnTable(playerIdentity: PlayerIdentity): List<BurracoScale> =
            when (val player = players.find { p -> p.identity() == playerIdentity }) {
                is PlayerInGame -> player.showScalesOnTable()
                else -> throw NoSuchElementException("Player $playerIdentity is not a player of this game ${identity()}")
            }

    fun showDiscardPile(): List<Card> = discardPile.showCards()

    fun showPlayerTurn(): PlayerIdentity = playerTurn

    fun showNumMazzettoAvailable(): Int = mazzettoDecks.list.size

    fun validatePlayerTurn(playerIdentity: PlayerIdentity): Unit =
            check(playerTurn == playerIdentity) {
                warnMsg("It's not the turn of the player $playerIdentity")
            }


    fun testInvariants() = invariantNumCardsInGame()

    // write
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