package com.abaddon83.burraco.shares.decks

import com.abaddon83.burraco.shares.decks.Ranks.Jolly
import org.scalatest.funsuite.AnyFunSuite

class DeckTest extends AnyFunSuite{

  test("create a deck with all ranks and all Suits and Jolly") {

    val expectedCardsSize = 54
    val expectedJolly = 2
    val expectedSuit =13
    val expectedRanks =4

    val cards = Deck.allRanksWithJollyCards()

    assert(cards.size == expectedCardsSize)
    assert(cards.count(card => card.rank == Ranks.Jolly && card.suit == Suits.Jolly) == expectedJolly,s"The deck doesn't contain ${expectedJolly} ${Jolly.label}")
    Suits.allSuit.foreach( suit => assert(cards.count(card => card.suit == suit) == expectedSuit,s"The card list doesn't contain ${expectedSuit} ${suit.icon} cards"))
    Ranks.fullRanks.foreach( rank => assert(cards.count(card => card.rank == rank) == expectedRanks,s"The card list doesn't contain ${expectedRanks} ${rank.label} cards"))

  }

  test("create a deck with all ranks and all Suits without Jolly") {

    val expectedCardsSize = 52
    val expectedJolly = 0
    val expectedSuit =13
    val expectedRanks =4

    val cards = Deck.allRanksCards()

    assert(cards.size == expectedCardsSize)
    assert(cards.count(card => card.rank == Ranks.Jolly && card.suit == Suits.Jolly) == expectedJolly,s"The deck doesn't contain ${expectedJolly} ${Jolly.label}")
    Suits.allSuit.foreach( suit => assert(cards.count(card => card.suit == suit) == expectedSuit,s"The card list doesn't contain ${expectedSuit} ${suit.icon} cards"))
    Ranks.fullRanks.foreach( rank => assert(cards.count(card => card.rank == rank) == expectedRanks,s"The card list doesn't contain ${expectedRanks} ${rank.label} cards"))
  }
}
