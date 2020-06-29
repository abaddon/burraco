package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.mazzettos

import com.abaddon83.burraco.shares.decks.Deck
import org.scalatest.funsuite.AnyFunSuite

class MazzettoDeckTest extends AnyFunSuite{

  test("Given 11 cards, when I create a Mazzetto, then I have a mazzetto") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,11)
    val mazzettoDeck = MazzettoDeck.build(cardList)

    val pozzettoCardList = mazzettoDeck.getCards()

    assert(cardList == pozzettoCardList)
    assert(cardList.size == pozzettoCardList.size)
  }

  test("Given 18 cards, when I create a Mazzetto, then I have a mazzetto") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,18)
    val mazzettoDeck = MazzettoDeck.build(cardList)

    val pozzettoCardList = mazzettoDeck.getCards()

    assert(cardList == pozzettoCardList)
    assert(cardList.size == pozzettoCardList.size)
  }

  test("Given 13 cards, when I create a Mazzetto, then I receive an error") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,13)
    assertThrows[AssertionError] {
      MazzettoDeck.build(cardList)
    }
  }

  test("Given a mazzetto, when I grab a card, then I receive an error") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,11)
    val mazzettoDeck = MazzettoDeck.build(cardList)
    assertThrows[UnsupportedOperationException] {
      mazzettoDeck.grabFirstCard()
    }
  }

  test("Given a mazzetto, when I grab all cards, then I receive an error") {
    val cardList = Deck.allRanksWithJollyCards().slice(0,11)
    val mazzettoDeck = MazzettoDeck.build(cardList)
    assertThrows[UnsupportedOperationException] {
      mazzettoDeck.grabAllCards()
    }
  }

}
