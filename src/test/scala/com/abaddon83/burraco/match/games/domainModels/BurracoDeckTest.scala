package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Ranks.Jolly
import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer

class BurracoDeckTest extends AnyFunSuite{

  test("create a Burraco Deck") {
    val deck = BurracoDeck()

    assert(deck.numCards() == 108)

    val listOfAllCards = ListBuffer[Card]()
    while(deck.numCards()>0){
      listOfAllCards.addOne(deck.grabFirstCard())
    }

    assert(listOfAllCards.size == 108)
    assert(listOfAllCards.count(card => card.rank == Ranks.Jolly && card.suit == Suits.Jolly) == 4,s"The deck doesn't contain 4 ${Jolly.label}")
    Suits.allSuit.foreach( suit => assert(listOfAllCards.count(card => card.suit == suit) == 26,s"The card list doesn't contain 26 ${suit.icon} cards"))
    Ranks.fullRanks.foreach( rank => assert(listOfAllCards.count(card => card.rank == rank) == 8,s"The card list doesn't contain 8 ${rank.label} cards"))
  }

  test("grabAllCard from the Deck should fail") {
    val deck = BurracoDeck()
    assertThrows[UnsupportedOperationException]{
      deck.grabAllCards()
    }
  }


}
