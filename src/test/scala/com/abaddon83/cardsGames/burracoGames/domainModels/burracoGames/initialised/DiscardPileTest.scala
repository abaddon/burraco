package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.shares.decks.{Card, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer

class DiscardPileTest extends AnyFunSuite{

  test("Given a list of card, when I create a discard pile, then I see the same cards in the discard pile") {
    val cardList = List(Card(Suits.Heart,Ranks.Ace),Card(Suits.Heart,Ranks.King))
    val discardPile = DiscardPile(cardList)
    val expectedSize = cardList.size

    assert(discardPile.numCards() == expectedSize)
  }

  test("Given a card, when I add it on the discard pile, then I see the card in the pile") {
    val cardList = List(Card(Suits.Heart,Ranks.Ace),Card(Suits.Heart,Ranks.King))
    val discardPile = DiscardPile(cardList)

    val cardToAdd = Card(Suits.Jolly,Ranks.Jolly)

    val expectedSize = cardList.size + 1

    val discardPileWithCardAdded = discardPile.addCard(cardToAdd)

    assert(discardPileWithCardAdded.numCards() == expectedSize)
    assert(discardPileWithCardAdded.grabAllCards().contains(cardToAdd))
  }


  test("given a discardPile, when I grab all cards, then the pile is empty") {
    val discardPile = DiscardPile(List(Card(Suits.Heart,Ranks.Ace),Card(Suits.Heart,Ranks.King)))
    assert(discardPile.numCards() == 2)

    val grabbedCards = discardPile.grabAllCards()

    val expectedCardsInthePile = 0
    val expectedCardsgrabbed = 2

    assert(discardPile.numCards() == expectedCardsInthePile)
    assert(grabbedCards.size == expectedCardsgrabbed)
  }

  test("given an empty discardPile, when I grab all cards, then receive an error") {

    val discardPile = DiscardPile(new ListBuffer[Card].empty)

    assertThrows[AssertionError]{
      discardPile.grabAllCards()
    }
  }

  test("given a discardPile, when I grab only a card, then receive an error"){

    val discardPile = DiscardPile(List(Card(Suits.Heart,Ranks.Ace),Card(Suits.Heart,Ranks.King)))

    assertThrows[UnsupportedOperationException]{
      discardPile.grabFirstCard()
    }
  }

}
