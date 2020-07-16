package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.decks.Ranks
import com.abaddon83.burracoGame.shared.decks.Suits
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.burracoGame.testUtils.BurracoGameInitTurnTestFactory
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoGameExecutionTurnBeginningTest {
    @Test
    fun `Given the right information, when I build a new Burraco game in the phase turn started, then I create it coreectly`() {
        val player1Id = PlayerIdentity.create()
        val gameIdentity = GameIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(gameIdentity = gameIdentity, player1Id = player1Id)
                .buildTurnPhaseStart()

        assert(game.identity() == gameIdentity)
    }

    @Test
    fun `Given a player during his turn, when pickUps a card from the deck, then I have a card more`() {
        val player1Id = PlayerIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .buildTurnPhaseStart()

        val expectedPlayerCards = game.playerCards(player1Id).size + 1

        val gameActual = game.pickUpACardFromDeck(player1Id)
        val playerCardsActual = gameActual.playerCards(player1Id)

        assert(expectedPlayerCards == playerCardsActual.size)
    }

    @Test
    fun `Given a player not during his turn, when pickUps a card from the deck, then I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer2Turn()
                .buildTurnPhaseStart()

        assertFailsWith(IllegalStateException::class) {
            game.pickUpACardFromDeck(player1Id)
        }
    }

    @Test
    fun `Given a player during his turn, when pickUps a card from the discard pile , then I have more cards`() {
        val player1Id = PlayerIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setDiscardPile(listOf(Card(Suits.Pike, Ranks.Eight)))
                .buildTurnPhaseStart()

        val discardPileSize = game.showDiscardPile().size
        val gameActual = game.pickUpCardsFromDiscardPile(player1Id)

        val expectedPlayerCards = game.playerCards(player1Id).size + discardPileSize

        assert(gameActual.playerCards(player1Id).size == expectedPlayerCards)
        assert(gameActual.showDiscardPile().isEmpty())
    }

    @Test
    fun `Given a player during his turn, when pickUps a card from the discard pile , then I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer2Turn()
                .buildTurnPhaseStart()

        assertFailsWith(IllegalStateException::class) {
            game.pickUpCardsFromDiscardPile(player1Id)
        }
    }
}