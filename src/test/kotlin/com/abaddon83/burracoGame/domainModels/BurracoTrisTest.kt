package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.commandModel.models.BurracoTris
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.commandModel.models.decks.Ranks
import com.abaddon83.burracoGame.commandModel.models.decks.Suits
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoTrisTest {

    @Test
    fun `given I have 3 cards with the same rank, when I create a tris then I have a new tris`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3
        val cards = ListCardsBuilder.allRanksWithJollyCards().filter{ c -> c . rank == burracoTrisRank}.take(burracoTrisSize)

        val actualBurracoTris = BurracoTris.create(cards)

        assert(actualBurracoTris.showCards().size == burracoTrisSize)
        assert(actualBurracoTris.showRank() == burracoTrisRank)
    }

    @Test
    fun `given I have 2 cards with the same rank, when I create a tris then I receive an error`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 2
        val cards = ListCardsBuilder.allRanksWithJollyCards().filter{ c -> c . rank == burracoTrisRank}.take(burracoTrisSize)

            assertFailsWith(IllegalStateException::class){
            BurracoTris.create(cards)
        }
    }

    @Test
    fun `given I have 3 cards with the different rank, when I create a tris then I receive an error`() {

        val cards = listOf(
                Card(suit = Suits.Heart, rank = Ranks.Eight),
                Card(suit = Suits.Heart, rank = Ranks.Eight),
                Card(suit = Suits.Heart, rank = Ranks.Three)
        )

        assertFailsWith(AssertionError::class){
            BurracoTris.create(cards)
        }
    }

    @Test
    fun `given I have 3 cards with the same rank and a jolly, when I create a tris then I have a new tris`() {
        val burracoTrisRank = Ranks.Five
        val cards = listOf(
                Card(suit = Suits.Heart, rank = burracoTrisRank),
                Card(suit = Suits.Heart, rank = burracoTrisRank),
                Card(suit = Suits.Jolly, rank = Ranks.Jolly)
        )
        val tris = BurracoTris.create(cards)

        assert(tris.showCards().size == cards.size)
        assert(tris.showRank() == burracoTrisRank)


    }

    @Test
    fun `given I have 3 cards with the same rank and a Two, when I create a tris then I have a new tris`() {
        val burracoTrisRank = Ranks.Five
        val cards = listOf(
                Card(suit = Suits.Heart, rank = burracoTrisRank),
                Card(suit = Suits.Heart, rank = burracoTrisRank),
                Card(suit = Suits.Heart, rank = Ranks.Two)
        )

        val tris = BurracoTris.create(cards)

        assert(tris.showCards().size == cards.size)
        assert(tris.showRank() == burracoTrisRank)

    }

    @Test
    fun `given I have 3 Jolly, when I create a tris then  I receive an error`() {
        val burracoTrisRank = Ranks.Jolly
        val cards = listOf(
                Card(suit = Suits.Jolly, rank = burracoTrisRank),
                Card(suit = Suits.Jolly, rank = burracoTrisRank),
                Card(suit = Suits.Jolly, rank = burracoTrisRank)
        )

        assertFailsWith(AssertionError::class){
            BurracoTris.create(cards)
        }

    }

    @Test
    fun `given I have 3 Two, when I create a tris then I receive an error`() {
        val burracoTrisRank = Ranks.Two
        val cards = listOf(
                Card(suit = Suits.Heart, rank = burracoTrisRank),
                Card(suit = Suits.Pike, rank = burracoTrisRank),
                Card(suit = Suits.Tile, rank = burracoTrisRank)
        )

        assertFailsWith(AssertionError::class){
            BurracoTris.create(cards)
        }

    }

    @Test
    fun `given a tris, when I add a card with the same rank, then the tris size is increased by one`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Heart, rank = burracoTrisRank))
        val burracoTris = createABurracoTrisWith(burracoTrisRank, burracoTrisSize)

        val actualBurracoTris = burracoTris.addCards(cards)

        assert(actualBurracoTris.showCards().size == burracoTrisSize + 1)
        assert(actualBurracoTris.showCards().contains(cards[0]))
    }

    @Test
    fun `given a tris, when I add a Jolly, then the tris size is increased by one`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Jolly, rank = Ranks.Jolly))
        val burracoTris = createABurracoTrisWith(burracoTrisRank, burracoTrisSize)

        val actualBurracoTris = burracoTris.addCards(cards)

        assert(actualBurracoTris.showCards().size == burracoTrisSize + 1)
        assert(actualBurracoTris.showCards().contains(cards[0]))
    }

    @Test
    fun `given a tris with a Jolly, when I add a Jolly, then I receive an error`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Jolly, rank = Ranks.Jolly))
        val burracoTris = createABurracoTrisWithAJollyAnd(burracoTrisRank, burracoTrisSize)

        assertFailsWith(IllegalStateException::class){
            burracoTris.addCards(cards)
        }
    }

    @Test
    fun `given a tris with a Jolly, when I add a card with rank Two, then I receive an error`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Heart, rank = Ranks.Two))
        val burracoTris = createABurracoTrisWithAJollyAnd(burracoTrisRank, burracoTrisSize)

        assertFailsWith(IllegalStateException::class){
            burracoTris.addCards(cards)
        }
    }

    @Test
    fun `given a tris that include a card with rank Two, when I add a Jolly, then I receive an error`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Jolly, rank = Ranks.Jolly))
        val burracoTris = createABurracoTrisWithATwoAnd(burracoTrisRank, burracoTrisSize)

        assertFailsWith(IllegalStateException::class){
            burracoTris.addCards(cards)
        }
    }

    @Test
    fun `given a tris that include a card with rank Two, when I add a card with rank Two, then I receive an error`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Heart, rank = Ranks.Two))
        val burracoTris = createABurracoTrisWithATwoAnd(burracoTrisRank, burracoTrisSize)

        assertFailsWith(IllegalStateException::class){
            burracoTris.addCards(cards)
        }
    }

    @Test
    fun `given a tris, when I add a card with a different rank, then I receive an error`() {
        val burracoTrisRank = Ranks.Five
        val burracoTrisSize = 3

        val cards = listOf(Card(suit = Suits.Heart, rank = Ranks.Three))
        val burracoTris = createABurracoTrisWith(burracoTrisRank, burracoTrisSize)

        assertFailsWith(IllegalStateException::class){
            burracoTris.addCards(cards)
        }
    }

    //TODO missing test related to the definition of a Burraco

    private fun createABurracoTrisWith(rank: Ranks.Rank, numCards: Int): BurracoTris =
        BurracoTris.create(
                cards = ListCardsBuilder.allRanksWithJollyCards().filter{ c -> c . rank == rank}.take(numCards)
        )


    private fun createABurracoTrisWithAJollyAnd(rank: Ranks.Rank, numCards: Int): BurracoTris =
        BurracoTris.create(
                cards = ListCardsBuilder.allRanksWithJollyCards().filter{ c -> c . rank == rank}.take(numCards - 1).plus(Card(suit = Suits.Jolly, rank = Ranks.Jolly))
        )


    private fun createABurracoTrisWithATwoAnd(rank: Ranks.Rank, numCards: Int): BurracoTris =

        BurracoTris.create(
                cards = ListCardsBuilder.allRanksWithJollyCards().filter{ c -> c . rank == rank}.take(numCards - 1).plus(Card(suit = Suits.Heart, rank = Ranks.Two))
        )

}