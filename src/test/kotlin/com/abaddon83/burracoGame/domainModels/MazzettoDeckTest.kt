package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.writeModel.models.MazzettoDeck
import com.abaddon83.burracoGame.writeModel.models.decks.ListCardsBuilder
import org.junit.Test
import kotlin.test.assertFailsWith

class MazzettoDeckTest {
    @Test
    fun `Given 11 cards, when I create a Mazzetto, then I have a mazzetto`() {
        val cardList = ListCardsBuilder.allRanksWithJollyCards().slice(IntRange(0,10))
        val mazzettoDeck = MazzettoDeck.create(cardList)

        val pozzettoCardList = mazzettoDeck.getCardList()

        assert(cardList == pozzettoCardList)
        assert(cardList.size == pozzettoCardList.size)
    }

    @Test
    fun `Given 18 cards, when I create a Mazzetto, then I have a mazzetto`() {
        val cardList = ListCardsBuilder.allRanksWithJollyCards().slice(IntRange(0,17))
        val mazzettoDeck = MazzettoDeck.create(cardList)

        val pozzettoCardList = mazzettoDeck.getCardList()

        assert(cardList == pozzettoCardList)
        assert(cardList.size == pozzettoCardList.size)
    }

    @Test
    fun `Given 13 cards, when I create a Mazzetto, then I receive an error`() {
        val cardList = ListCardsBuilder.allRanksWithJollyCards().slice(IntRange(0,13))
        assertFailsWith(IllegalArgumentException::class) {
            MazzettoDeck.create(cardList)
        }
    }

    @Test
    fun `Given a mazzetto, when I grab a card, then I receive an error`() {
        val cardList = ListCardsBuilder.allRanksWithJollyCards().slice(IntRange(0,10))
        val mazzettoDeck = MazzettoDeck.create(cardList)
        assertFailsWith(UnsupportedOperationException::class) {
            mazzettoDeck.grabFirstCard()
        }
    }

    @Test
    fun `Given a mazzetto, when I grab all cards, then I receive an error`() {
        val cardList = ListCardsBuilder.allRanksWithJollyCards().slice(IntRange(0,10))
        val mazzettoDeck = MazzettoDeck.create(cardList)
        assertFailsWith(UnsupportedOperationException::class) {
            mazzettoDeck.grabAllCards()
        }
    }

}