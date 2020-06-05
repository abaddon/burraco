package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer

class DiscardPileTest extends AnyFunSuite{

  test("Init DiscardPile with a list of Card") {
    val cardList = List(Card(Suits.Heart,Ranks.Ace),Card(Suits.Heart,Ranks.King))
    val discardPile = DiscardPile(cardList)

    assert(discardPile.numCards() == 2)
  }

  test("add a card on the DiscardPile") {
    val cardList = List(Card(Suits.Heart,Ranks.Ace),Card(Suits.Heart,Ranks.King))
    val discardPile = DiscardPile(cardList)
    assert(discardPile.numCards() == 2)

    val cardToAdd = Card(Suits.Jolly,Ranks.Jolly)

    val discardPileWithCardAdded = discardPile.addCard(cardToAdd)

    assert(discardPileWithCardAdded.numCards() == 3)
    assert(discardPileWithCardAdded.grabAllCards().contains(cardToAdd))
  }

  test("add a card on an empty discard pile") {

    val discardPile = DiscardPile(new ListBuffer[Card])
    assert(discardPile.numCards() == 0)

    val cardToAdd = Card(Suits.Jolly,Ranks.Jolly)

    val discardPileWithCardAdded = discardPile.addCard(cardToAdd)

    assert(discardPile.numCards() == 1)
    assert(discardPileWithCardAdded.grabAllCards().contains(cardToAdd))
  }

  test("grabAllCards") {
    val card1 = Card(Suits.Heart,Ranks.Ace)
    val card2 = Card(Suits.Heart,Ranks.King)
    val cardList = List(card1,card2)

    val discardPile = DiscardPile(cardList)
    assert(discardPile.numCards() == 2)

    val cardToAdd = Card(Suits.Jolly,Ranks.Jolly)

    val discardPileWithCardAdded = discardPile.addCard(cardToAdd)

    val grabbedCards = discardPileWithCardAdded.grabAllCards()

    assert(discardPileWithCardAdded.numCards() == 0)
    assert(grabbedCards.size == 3)
    assert(grabbedCards.contains(cardToAdd))
    assert(grabbedCards.contains(card1))
    assert(grabbedCards.contains(card2))
  }

  test("grabAllCards from an empty list") {

    val discardPile = DiscardPile(new ListBuffer[Card])

    assert(discardPile.numCards() == 0)

    assertThrows[AssertionError]{
      discardPile.grabAllCards()
    }
  }

  test("grab a card should fail"){
    val card1 = Card(Suits.Heart,Ranks.Ace)
    val card2 = Card(Suits.Heart,Ranks.King)
    val discardPile = DiscardPile(List(card1,card2))
    assert(discardPile.numCards() == 2)

    assertThrows[UnsupportedOperationException]{
      discardPile.grabFirstCard()
    }
  }

}
