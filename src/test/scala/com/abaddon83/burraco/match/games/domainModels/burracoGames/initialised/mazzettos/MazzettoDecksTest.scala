package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.mazzettos

import com.abaddon83.burraco.shares.decks.Deck
import org.scalatest.funsuite.AnyFunSuite

class MazzettoDecksTest extends AnyFunSuite{

  test("Given 2 mazzettiDeck, when I create a MazzettoDecks, then I have it"){
    val mazzettoDeck1 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    val mazzettoDeck2 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    val list = List(mazzettoDeck1,mazzettoDeck2)

    val expectedNumCards = 22

    val actualMazzettoDecks = MazzettoDecks.build(list)
    assert(actualMazzettoDecks.numCards()==expectedNumCards)
  }

  test("Given 1 mazzettiDeck, when I create a MazzettoDecks, then I receive an error"){
    val mazzettoDeck1 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    val list = List(mazzettoDeck1)


    assertThrows[AssertionError]{
      MazzettoDecks.build(list)
    }
  }

  test("Given 3 mazzettiDeck, when I create a MazzettoDecks, then I receive an error"){
    val mazzettoDeck1 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    val list = List(mazzettoDeck1,mazzettoDeck1,mazzettoDeck1)

    assertThrows[AssertionError]{
      MazzettoDecks.build(list)
    }
  }

  test("Given a MazzettoDecks full, when ask the first Mazzetto, I receive the first"){
    val mazzettoDeck1 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    val mazzettoDeck2 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    assert(mazzettoDeck1 != mazzettoDeck2)
    val list = List(mazzettoDeck1,mazzettoDeck2)

    val expectedNumCards = 11

    val mazzettoDecks = MazzettoDecks.build(list)
    val actualMazzettoDeck = mazzettoDecks.firstMazzettoAvailable()
    val actualMazzettoDecks = mazzettoDecks.mazzettoTaken(actualMazzettoDeck)

    assert(actualMazzettoDeck == mazzettoDeck2)
    assert(actualMazzettoDecks.firstMazzettoAvailable() != actualMazzettoDeck)
    assert(actualMazzettoDecks.numCards() == expectedNumCards)
  }

  test("Given a MazzettoDecks empty, when ask the first Mazzetto, I receive an error"){
    val mazzettoDeck1 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    val mazzettoDeck2 =  MazzettoDeck.build(Deck.allRanksWithJollyCards().take(11))
    assert(mazzettoDeck1 != mazzettoDeck2)
    val list = List(mazzettoDeck1,mazzettoDeck2)
    val mazzettoDecks = MazzettoDecks.build(list)
    val actualMazzettoDecks = mazzettoDecks
      .mazzettoTaken(mazzettoDeck1)
      .mazzettoTaken(mazzettoDeck2)

    assertThrows[AssertionError]{
      actualMazzettoDecks.firstMazzettoAvailable()
    }
  }

}
