package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.Deck
import org.scalatest.funsuite.AnyFunSuite

class PozzettoDeckTest extends AnyFunSuite{

  test("Create a potezzetto Deck with 11 cards") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,11)
    val pozzettoDeck = PozzettoDeck.build(cardList)

    val pozzettoCardList = pozzettoDeck.grabAllCards()

    assert(cardList == pozzettoCardList)
    assert(cardList.size == pozzettoCardList.size)
    assert(pozzettoDeck.numCards() == 0)
  }

  test("Create a potezzetto Deck with 18 cards") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,18)
    val pozzettoDeck = PozzettoDeck.build(cardList)

    val pozzettoCardList = pozzettoDeck.grabAllCards()

    assert(cardList == pozzettoCardList)
    assert(cardList.size == pozzettoCardList.size)
    assert(pozzettoDeck.numCards() == 0)
  }

  test("Create a potezzetto Deck with 13 cards, should fail") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,13)
    assertThrows[AssertionError] {
      PozzettoDeck.build(cardList)
    }
  }

  test("try to grab a card from the deck, should fail") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,11)
    val pozzettoDeck = PozzettoDeck.build(cardList)
    assertThrows[UnsupportedOperationException] {
      pozzettoDeck.grabFirstCard()
    }
  }

}
