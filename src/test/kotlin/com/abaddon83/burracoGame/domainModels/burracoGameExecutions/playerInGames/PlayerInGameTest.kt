package com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames

import com.abaddon83.burracoGame.writeModel.models.BurracoScale
import com.abaddon83.burracoGame.writeModel.models.BurracoTris
import com.abaddon83.burracoGame.writeModel.models.MazzettoDeck
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits
import com.abaddon83.burracoGame.testUtils.PlayerInGameTestFactory
import org.junit.Test
import kotlin.test.assertFailsWith

class PlayerInGameTest {
    @Test
    fun `given a Mazzetto, when I take it  for the first time, then I have it on my cards`() {

        val myCards = listOf(Card(Suits.Heart, Ranks.Six))
        val playerInGame = PlayerInGameTestFactory.create().withCards(myCards).build()
        val mazzetto: MazzettoDeck = MazzettoDeck.create(ListCardsBuilder.allRanksCards().take(11))

        val expectedMyCardsSize = mazzetto.numCards() + myCards.size
        val expectedMazzettoTaken = true
        val expectedAllCardsSize = myCards.size + mazzetto.numCards()

        val actualPlayerInGame = playerInGame.pickUpMazzetto(mazzetto)

        assert(actualPlayerInGame.isMazzettoTaken() == expectedMazzettoTaken)
        assert(actualPlayerInGame.showMyCards().size == expectedMyCardsSize)
        assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)
    }

    @Test
    fun `given a Mazzetto, when I take it  for the second time, then I receive an error`() {
        val myCards = listOf(Card(Suits.Heart, Ranks.Six))
        val playerInGame = PlayerInGameTestFactory.create().withCards(myCards).build()
        val mazzetto: MazzettoDeck = MazzettoDeck.create(ListCardsBuilder.allRanksCards().take(11))
        val mazzetto2: MazzettoDeck = MazzettoDeck.create(ListCardsBuilder.allRanksCards().take(11))

        assertFailsWith(IllegalStateException::class) {
            playerInGame.pickUpMazzetto(mazzetto).pickUpMazzetto(mazzetto2)
        }
    }

    @Test
    fun `given a Tris on My cards, when I drop it, then I see it on the table`() {
        val trisCards = ListCardsBuilder.allRanksCards().filter { c -> c.rank == Ranks.Seven }.take(4)
        val myCards = ListCardsBuilder.allRanksCards().take(5).plus(trisCards)
        val playerInGame = PlayerInGameTestFactory.create().withCards(myCards).build()
        val trisToDrop = BurracoTris.create(trisCards)

        println("trisCards size: ${trisCards.size}, ${trisCards}")
        println("myCards size: ${myCards.size}, ${myCards}")

        val expectedMyCardsSize = 5
        val expectedTrisOnTable = trisToDrop
        val expectedAllCardsSize = myCards.size

        val actualPlayerInGame = playerInGame.dropATris(trisToDrop)

        assert(actualPlayerInGame.showMyCards().size == expectedMyCardsSize){"${actualPlayerInGame.showMyCards().size} == $expectedMyCardsSize"}
        assert(actualPlayerInGame.showTrisOnTable().first() == expectedTrisOnTable)
        assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)

    }

    @Test
    fun `given a Scale on My cards, when I drop it, then I see it on the table`() {
        val scaleCards = listOf(
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Six),
                Card(Suits.Heart, Ranks.Seven)
        )
        val myCards = ListCardsBuilder.allRanksCards().take(5).plus(scaleCards)
        val playerInGame = PlayerInGameTestFactory.create().withCards(myCards).build()
        val scaleToDrop = BurracoScale.create(scaleCards)

        val expectedMyCardsSize = 5
        val expectedScaleOnTable = scaleToDrop
        val expectedAllCardsSize = myCards.size

        val actualPlayerInGame = playerInGame.dropAScale(scaleToDrop)

        assert(actualPlayerInGame.showMyCards().size == expectedMyCardsSize)
        assert(actualPlayerInGame.showScalesOnTable().first() == expectedScaleOnTable)
        assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)

    }

    @Test
    fun `given some cards, when I drop one of them, then I have a card less on my hand`() {
        val cardToDrop = ListCardsBuilder.allRanksCards().take(1)
        val myCards = ListCardsBuilder.allRanksCards().filterNot { p -> p == cardToDrop.first() }.take(5).plus(cardToDrop)
        val playerInGame = PlayerInGameTestFactory.create().withCards(myCards).build()

        val expectedMyCardSize = myCards.size - 1

        val actualPlayerInGame = playerInGame.dropACard(cardToDrop.first())
        val expectedCardDropped = cardToDrop
        val expectedAllCardsSize = myCards.size - 1

        assert(actualPlayerInGame.showMyCards().size == expectedMyCardSize)
        assert(!actualPlayerInGame.showMyCards().containsAll(expectedCardDropped))
        assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)
    }

    @Test
    fun `given a card to append on a Scale on table, when I append the card, then the card is on the burraco`() {
        val cardsScale = listOf(
                Card(Suits.Heart, Ranks.Four),
                Card(Suits.Heart, Ranks.Five),
                Card(Suits.Heart, Ranks.Six),
                Card(Suits.Heart, Ranks.Seven)
        )
        val cardToDrop = Card(Suits.Heart, Ranks.Eight)
        var deck = ListCardsBuilder.allRanksCards().minus(cardsScale).minus(listOf(cardToDrop))
        val burracoTris = BurracoTris.create(deck.filter { c -> c.rank == Ranks.Three }.take(4))
        deck = deck.minus(burracoTris.showCards())
        val myCards = deck.take(5).plus(listOf(cardToDrop))
        //deck.minus(myCards)

        val burracoScale = BurracoScale.create(cardsScale)

        val playerInGame = PlayerInGameTestFactory.create()
                .withCards(myCards)
                .withScalaOnTable(burracoScale)
                .withTrisOnTable(burracoTris)
                .build()

        val expectedScaleSize = burracoScale.showCards().size + 1
        val expectedMyCardsSize = myCards.size - 1
        val expectedAllCardsSize = myCards.size + burracoScale.showCards().size + burracoTris.showCards().size

        val actualPlayerInGame = playerInGame.appendACardOnBurracoDropped(burracoScale.identity(), listOf(cardToDrop))

        assert(actualPlayerInGame.showScalesOnTable().first().showCards().contains(cardToDrop))
        assert(actualPlayerInGame.showScalesOnTable().first().showCards().size == expectedScaleSize)
        assert(actualPlayerInGame.showScalesOnTable().first().showCards().size == expectedMyCardsSize)
        assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)
    }
}








