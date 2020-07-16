package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.shared.decks.*

data class BurracoDeck private constructor(override val cards: MutableList<Card>) : Deck {

    override fun grabAllCards(): List<Card> = throw UnsupportedOperationException("You cannot grab all cards from the Deck")

    override fun grabFirstCard(): Card = super.grabFirstCard()
    fun getFirstCard(): Card = cards.first()

    fun shuffle(): BurracoDeck = BurracoDeck(cards.shuffled().toMutableList())

    companion object Factory {
        fun create(cards: List<Card>): BurracoDeck = BurracoDeck(cards.toMutableList())
        fun create(): BurracoDeck {
            val cards: List<Card> = listOf(
                    ListCardsBuilder.allRanksWithJollyCards(),
                    ListCardsBuilder.allRanksWithJollyCards()
            ).flatten()

            assert(cards.size == 108)
            assert(cards.count {card -> card.rank == Ranks.Jolly && card.suit == Suits.Jolly} == 4){"The deck doesn't contain 4 ${Ranks.Jolly.label}"}
            Suits.allSuit.forEach { suit ->
                assert(cards.count { card -> card.suit == suit} == 26){"The card list doesn't contain 26 ${suit.icon} cards"}
            }
            Ranks.fullRanks.forEach{ rank ->
                assert(cards.count {card -> card.rank == rank} == 8){"The card list doesn't contain 8 ${rank.label} cards"}
            }

            return BurracoDeck(cards.toMutableList())
        }
    }
}