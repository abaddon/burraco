package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoDeckTest {
    @Test
fun `Given a Burraco Deck, when I count the carts, then the carts are 108`() {
        val deck = BurracoDeck.create()

        assert(deck.numCards() == 108)

        val listOfAllCards = mutableListOf<Card>()
        while(deck.numCards()>0){
            listOfAllCards.add(deck.grabFirstCard())
        }

        assert(listOfAllCards.size == 108)
        assert(listOfAllCards.count{card -> card.rank == Ranks.Jolly && card.suit == Suits.Jolly} == 4) { "The deck doesn't contain 4 ${Ranks.Jolly.label}" }
        Suits.allSuit.forEach{ suit -> assert(listOfAllCards.count{card -> card.suit == suit} == 26) { "The card list doesn't contain 26 ${suit.icon} cards" }}
        Ranks.fullRanks.forEach{ rank -> assert(listOfAllCards.count{card -> card.rank == rank} == 8) { "The card list doesn't contain 8 ${rank.label} cards" }}
    }

    @Test
fun `Given a Burraco Deck, when I shuffle it, then cards order is changed`() {
        val deck = BurracoDeck.create()
        val firstCard = deck.grabFirstCard()

        val actualDeck = deck.shuffle()
        val actualFirstCard = deck.grabFirstCard()

        assert(firstCard != actualFirstCard)
    }

    @Test
fun `Given a Burraco Deck, when I grab all cards, then I receive an error`() {
        val deck = BurracoDeck.create()
        assertFailsWith(UnsupportedOperationException::class) {
            deck.grabAllCards()
        }
    }

    @Test
fun `Given a Burraco Deck, when I grab the first card, then I have a card`() {
        val deck = BurracoDeck.create()

        deck.grabFirstCard()

        assert(deck.numCards() == 107)
    }
}