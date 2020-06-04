package com.abaddon83.burraco.shares.decks

import com.abaddon83.burraco.shares.decks.Ranks.Jolly
import org.scalatest.funsuite.AnyFunSuite

class DeckTest extends AnyFunSuite{

  test("create a double deck with Jolly") {
    val deck = Deck.buildDoubleFullDeckWithJolly()

    assert(deck.cards.size == 108)
    assert(deck.cards.count(card => card.rank == Ranks.Jolly && card.suit == Suits.Jolly) == 4,s"The deck doesn't contain 4 ${Jolly.label}")
    Suits.allSuit.foreach( suit => assert(deck.cards.count(card => card.suit == suit) == 26,s"The card list doesn't contain 26 ${suit.icon} cards"))
    Ranks.fullRanks.foreach( rank => assert(deck.cards.count(card => card.rank == rank) == 8,s"The card list doesn't contain 8 ${rank.label} cards"))

  }
}
