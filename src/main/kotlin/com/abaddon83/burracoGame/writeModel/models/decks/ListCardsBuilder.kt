package com.abaddon83.burracoGame.writeModel.models.decks

object ListCardsBuilder {

    fun allRanksWithJollyCards(): List<Card> {
        val cards = listOf(allRanksCards(), addJolly()).flatten()
        assert(cards.size == 54)
        return cards.shuffled()
    }

    fun allRanksCards(): List<Card> {
        val cards = Suits.allSuit.map { suit -> buildCardSuit(suit, Ranks.fullRanks) }.flatten()
        assert(cards.size == 52) { "The card list has to contain exactly 52 cards" }
        Suits.allSuit.forEach { suit ->
            assert(cards.count { card -> card.suit == suit } == 13) { "The card list doesn't contain 13 ${suit.icon} cards" }
        }
        Ranks.fullRanks.forEach { rank ->
            assert(cards.count { card -> card.rank == rank } == 4) { "The card list doesn't contain 4 ${rank.label} cards" }
        }
        return cards.shuffled()
    }

    fun addJolly(): List<Card> {
        val cards = listOf(Card(Suits.Jolly, Ranks.Jolly), Card(Suits.Jolly, Ranks.Jolly))
        assert(cards.size == 2) { "The card list has to contain exactly 2 cards" }
        assert(cards.count { card -> card.rank == Ranks.Jolly && card.suit == Suits.Jolly } == 2) { "The card list doesn't contain 2 ${Ranks.Jolly.label} cards" }
        return cards.shuffled()
    }

    private fun buildCardSuit(suit: Suits.Suit, ranks: List<Ranks.Rank>): List<Card> =
            ranks.map { rank -> Card(suit, rank) }.toList()

}