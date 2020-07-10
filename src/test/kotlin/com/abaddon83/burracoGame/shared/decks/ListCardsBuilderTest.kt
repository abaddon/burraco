package com.abaddon83.burracoGame.shared.decks

import org.junit.Test

class ListCardsBuilderTest{

    @Test
    fun `create a deck with all ranks and all Suits and Jolly`() {

        val expectedCardsSize = 54
        val expectedJolly = 2
        val expectedSuit =13
        val expectedRanks =4

        val cards = ListCardsBuilder.allRanksWithJollyCards()

        assert(cards.size == expectedCardsSize)
        assert(cards.count {card -> card.rank == Ranks.Jolly && card.suit == Suits.Jolly} == expectedJolly) { "The deck doesn't contain $expectedJolly ${Ranks.Jolly.label}" }
        Suits.allSuit.forEach { suit ->
            assert(cards.count{card -> card.suit == suit} == expectedSuit) { "The card list doesn't contain $expectedSuit ${suit.icon} cards"}
        }
        Ranks.fullRanks.forEach{ rank ->
            assert(cards.count{card -> card.rank == rank} == expectedRanks){ "The card list doesn't contain $expectedRanks ${rank.label} cards"}
        }

    }
    @Test
    fun `"create a deck with all ranks and all Suits without Jolly`() {

        val expectedCardsSize = 52
        val expectedJolly = 0
        val expectedSuit =13
        val expectedRanks =4

        val cards = ListCardsBuilder.allRanksCards()

        assert(cards.size == expectedCardsSize)
        assert(cards.count{card -> card.rank == Ranks.Jolly && card.suit == Suits.Jolly} == expectedJolly){"The deck doesn't contain $expectedJolly ${Ranks.Jolly.label}"}
        Suits.allSuit.forEach{ suit ->
            assert(cards.count{card -> card.suit == suit} == expectedSuit){"The card list doesn't contain $expectedSuit ${suit.icon} cards"}
            }
        Ranks.fullRanks.forEach{ rank ->
            assert(cards.count{card -> card.rank == rank} == expectedRanks){"The card list doesn't contain $expectedRanks ${rank.label} cards"}
        }
    }
}
