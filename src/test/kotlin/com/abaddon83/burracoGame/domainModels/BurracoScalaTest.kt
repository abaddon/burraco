package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.commandModel.models.BurracoScale
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.Ranks
import com.abaddon83.burracoGame.commandModel.models.decks.Suits
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoScalaTest {

    @Test
    fun `given I have 5 cards with the same suit, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.King),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten),
                Card(Suits.Heart, Ranks.Nine)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.King)
        assert(sortedScale.showCards()[2].rank == Ranks.Jack)
        assert(sortedScale.showCards().last().rank == Ranks.Nine)
    }

    @Test
    fun `given 5 cards from K to 8 with 9 missing, when I create a scale then I receive an error`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.King),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten),
                Card(Suits.Heart, Ranks.Eight)
        ).shuffled()
        assertFailsWith(AssertionError::class) {
            BurracoScale.create(cards)
        }
    }

    @Test
    fun `given 6 cards from K to 8 with 9 missing and a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.King),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten),
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Eight)
        ).shuffled()

        val sortedScale = BurracoScale.create(cards)


        assert(sortedScale.showCards().first().rank == Ranks.King)
        assert(sortedScale.showCards()[2].rank == Ranks.Jack)
        assert(sortedScale.showCards()[4].rank == Ranks.Jolly)
        assert(sortedScale.showCards().last().rank == Ranks.Eight)
    }

    @Test
    fun `given 5 cards from 5 to 1, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Five)
        assert(sortedScale.showCards()[2].rank == Ranks.Three)
        assert(sortedScale.showCards()[3].rank == Ranks.Two)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 5 cards from 5 to 1 and a 2 with a different Suit, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Tile, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Five)
        assert(sortedScale.showCards()[2].rank == Ranks.Three)
        assert(sortedScale.showCards()[3].rank == Ranks.Two)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 7 cards from 7 to 1 with a card missing and a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Seven),
                Card(Suits.Heart, Ranks.Six),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Seven)
        assert(sortedScale.showCards()[2].rank == Ranks.Five)
        assert(sortedScale.showCards()[4].rank == Ranks.Three)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 7 cards from 7 to 1 with a card missing and 2 Two, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Seven),
                Card(Suits.Heart, Ranks.Six),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Tile, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Seven)
        assert(sortedScale.showCards()[2].rank == Ranks.Five)
        assert(sortedScale.showCards()[4].rank == Ranks.Three)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 7 cards from A to 8 with the card 9 missing and a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Ace),
                Card(Suits.Heart, Ranks.King),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten),
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Eight)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Ace)
        assert(sortedScale.showCards()[2].rank == Ranks.Queen)
        assert(sortedScale.showCards()[4].rank == Ranks.Ten)
        assert(sortedScale.showCards().last().rank == Ranks.Eight)
    }

    @Test
    fun `given 5 cards from A to 10 with the card K missing and a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Ace),
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Ace)
        assert(sortedScale.showCards()[2].rank == Ranks.Queen)
        assert(sortedScale.showCards().last().rank == Ranks.Ten)
    }

    @Test
    fun `given 4 cards from A to 10 with the card K missing, when I create a scale then I receive an error`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Ace),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten)
        ).shuffled()
        assertFailsWith(AssertionError::class) {
            BurracoScale.create(cards)
        }
    }

    @Test
    fun `given 6 cards from 7 to A with the card 4 missing and with a card 2 with a different suit, when I create a scale then I receive an error`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Seven),
                Card(Suits.Heart, Ranks.Six),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Tile, Ranks.Two),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        assertFailsWith(AssertionError::class) {
            BurracoScale.create(cards)
        }
    }

    @Test
    fun `given 5 cards from 5 to A with the card 3 missing and with 2 cards 2, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Tile, Ranks.Two),
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Five)
        assert(sortedScale.showCards()[2].rank == Ranks.Two && sortedScale.showCards()[2].suit == Suits.Tile)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 5 cards from 5 to A with the card 4 missing and a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Five)
        assert(sortedScale.showCards()[2].rank == Ranks.Jolly && sortedScale.showCards()[2].suit == Suits.Jolly)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 4 cards from K to 10 with no card missing and a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.King),
                Card(Suits.Heart, Ranks.Queen),
                Card(Suits.Heart, Ranks.Jack),
                Card(Suits.Heart, Ranks.Ten)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(
                (sortedScale.showCards().first().rank == Ranks.Jolly && sortedScale.showCards().last().rank == Ranks.Ten) ||
                        (sortedScale.showCards().first().rank == Ranks.King && sortedScale.showCards().last().rank == Ranks.Jolly)
        )
    }

    @Test
    fun `given 5 cards from 5 to A with a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Ace)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Jolly)
        assert(sortedScale.showCards()[2].rank == Ranks.Four)
        assert(sortedScale.showCards().last().rank == Ranks.Ace)
    }

    @Test
    fun `given 4 cards from 6 to 2 with a Jolly, when I create a scale then I have a new scale`() {
        val cards = listOf(
                Card(Suits.Jolly, Ranks.Jolly),
                Card(Suits.Heart, Ranks.Six),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Two)
        ).shuffled()
        val sortedScale = BurracoScale.create(cards)

        assert(sortedScale.showCards().first().rank == Ranks.Six)
        assert(sortedScale.showCards()[2].rank == Ranks.Four)
        assert(sortedScale.showCards().last().rank == Ranks.Two)
    }

    @Test
    fun `given a scale from 3 to 5, when I add a card 6 with the same suit, then the scale increase the size`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(Card(Suits.Heart, Ranks.Six))

        val actualScale = scale.addCards(cardToAdd)

        assert(actualScale.showCards().size == cards.size + 1)
    }

    @Test
    fun `given a scale from 3 to 5, when I add a card 6 with the different suit, then the scale return an error`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(Card(Suits.Tile, Ranks.Six))

        assertFailsWith(IllegalStateException::class) {
            scale.addCards(cardToAdd)
        }

    }

    @Test
    fun `given a scale from 3 to 5, when I add a card Jolly, then the scale increase the size`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(Card(Suits.Jolly, Ranks.Jolly))

        val actualScale = scale.addCards(cardToAdd)

        assert(actualScale.showCards().size == cards.size + 1)
    }

    @Test
    fun `given a scale from 3 to 5, when I add a card 7 with the the same suit, then the scale return an error`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(Card(Suits.Tile, Ranks.Six))

        assertFailsWith(IllegalStateException::class) {
            scale.addCards(cardToAdd)
        }
    }

    @Test
    fun `given a scale from 2 to 5, when I add a card 7 with the the same suit, then the scale increase the size`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(Card(Suits.Heart, Ranks.Seven))

        val actualScale = scale.addCards(cardToAdd)

        assert(actualScale.showCards().size == cards.size + 1)
    }

    @Test
    fun `given a scale from 2 to 5, when I add a card 7 with the the different suit, then the scale fail`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(Card(Suits.Tile, Ranks.Six))

        assertFailsWith(IllegalStateException::class) {
            scale.addCards(cardToAdd)
        }
    }

    @Test
    fun `given a scale from 2 to 5, when I add 2 cards 7 and Jolly with the the same suit, then the scale increase the siz`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(
                Card(Suits.Heart, Ranks.Seven),
                Card(Suits.Jolly, Ranks.Jolly)
        )

        val actualScale = scale.addCards(cardToAdd)

        assert(actualScale.showCards().size == cards.size + 2)
    }

    @Test
    fun `given a scale from 2 to 4, when I add 2 cards 7 and Jolly with the the same suit, then the scale return an error`() {
        val cards = listOf(
                Card(Suits.Heart, Ranks.Two),
                Card(Suits.Heart, Ranks.Three),
                Card(Suits.Heart, Ranks.Four)
        )
        val scale = BurracoScale.create(cards)
        val cardToAdd = listOf(
                Card(Suits.Heart, Ranks.Seven),
                Card(Suits.Jolly, Ranks.Jolly)
        )

        assertFailsWith(AssertionError::class) {
            scale.addCards(cardToAdd)
        }
    }

}