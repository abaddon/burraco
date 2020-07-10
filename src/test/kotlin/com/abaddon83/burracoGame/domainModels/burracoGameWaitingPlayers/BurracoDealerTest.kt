package com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import org.junit.Test

class BurracoDealerTest {
    @Test
    fun `check BurracoCardsDealt for 2 players`() {
        val expectedDiscardPileSize = 1
        val expectedFirstPozzettoSize = 11
        val expectedSecondPozzettoSize = 11
        val expectedDeckSize = 63
        val expectedPlayerCards = 11
        val expectedPlayers = 2

        val game = BurracoGame.create(GameIdentity.create())
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        val burracoCardsDealt = BurracoDealer.create(game)

        assert(burracoCardsDealt.discardPile.numCards() == expectedDiscardPileSize)
        assert(burracoCardsDealt.mazzettoDecks.list[0].numCards() == expectedFirstPozzettoSize)
        assert(burracoCardsDealt.mazzettoDecks.list[1].numCards() == expectedSecondPozzettoSize)
        assert(burracoCardsDealt.burracoDeck.numCards() == expectedDeckSize)
        burracoCardsDealt.playersCards.values.forEach { list ->
            assert(list.size == expectedPlayerCards)
        }
        assert(burracoCardsDealt.playersCards.size == expectedPlayers)

    }

    @Test
    fun `check BurracoCardsDealt for 3 players`() {
        val expectedDiscardPileSize = 1
        val expectedFirstPozzettoSize = 18
        val expectedSecondPozzettoSize = 11
        val expectedDeckSize = 45
        val expectedPlayerCards = 11
        val expectedPlayers = 3

        val game = BurracoGame.create(GameIdentity.create())
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        val burracoCardsDealt = BurracoDealer.create(game)

        assert(burracoCardsDealt.discardPile.numCards() == expectedDiscardPileSize)
        assert(burracoCardsDealt.mazzettoDecks.list[0].numCards() == expectedFirstPozzettoSize)
        assert(burracoCardsDealt.mazzettoDecks.list[1].numCards() == expectedSecondPozzettoSize)
        assert(burracoCardsDealt.burracoDeck.numCards() == expectedDeckSize)
        burracoCardsDealt.playersCards.values.forEach { list ->
            assert(list.size == expectedPlayerCards)
        }
        assert(burracoCardsDealt.playersCards.size == expectedPlayers)

    }

    @Test
    fun `check BurracoCardsDealt for 4 players`() {
        val expectedDiscardPileSize = 1
        val expectedFirstPozzettoSize = 11
        val expectedSecondPozzettoSize = 11
        val expectedDeckSize = 41
        val expectedPlayerCards = 11
        val expectedPlayers = 4

        val game = BurracoGame.create(GameIdentity.create())
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        val burracoCardsDealt = BurracoDealer.create(game)

        assert(burracoCardsDealt.discardPile.numCards() == expectedDiscardPileSize)
        assert(burracoCardsDealt.mazzettoDecks.list[0].numCards() == expectedFirstPozzettoSize)
        assert(burracoCardsDealt.mazzettoDecks.list[1].numCards() == expectedSecondPozzettoSize)
        assert(burracoCardsDealt.burracoDeck.numCards() == expectedDeckSize)
        burracoCardsDealt.playersCards.values.forEach { list ->
            assert(list.size == expectedPlayerCards)
        }
        assert(burracoCardsDealt.playersCards.size == expectedPlayers)

    }
}