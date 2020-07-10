package com.abaddon83.burracoGame.shared.decks

interface Deck {
    val cards: MutableList<Card>

    fun numCards(): Int = cards.size

    fun grabFirstCard(): Card = cards.removeAt(0)

    fun grabAllCards(): List<Card> {
        val grabbedCards = cards.toList()
        cards.removeAll(grabbedCards)
        assert(cards.size == 0)
        return grabbedCards
    }

}