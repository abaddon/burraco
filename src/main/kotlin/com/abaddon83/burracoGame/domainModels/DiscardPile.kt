package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.decks.Deck

data class DiscardPile private constructor(override val cards: MutableList<Card>): Deck {

    override fun grabAllCards(): List<Card> {
        assert(cards.size >0){"The DiscardPile is empty, you can't grab a card from here"}
        return super.grabAllCards()
    }

    override fun grabFirstCard(): Card = throw UnsupportedOperationException("You cannot grab only one card from the DiscardPile")


    fun addCard(card: Card): DiscardPile {
        cards.add(card)
        return this
    }

    fun showCards(): List<Card> = cards.toList()

    companion object Factory {
        fun create(cards: List<Card>): DiscardPile {
            return DiscardPile(cards = cards.toMutableList())
        }
    }
}