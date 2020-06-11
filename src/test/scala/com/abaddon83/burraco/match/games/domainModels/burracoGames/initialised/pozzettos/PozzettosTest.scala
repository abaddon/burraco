package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.Deck
import org.scalatest.funsuite.AnyFunSuite

class PozzettosTest extends AnyFunSuite{

  test("create Pozzettos with a 11 cards PozzettoDeck and an 18 cards PozzettoDeck"){
    val list = List(
      PozzettoDeck.build(Deck.allRanksWithJollyCards().slice(0,18)),
      PozzettoDeck.build(Deck.allRanksWithJollyCards().slice(1,12))
    )
    val pozzettos = Pozzettos.build(list)
    assert(pozzettos.numCards()== 18+11)
    assert(pozzettos.firstPozzettoAvailable().size == 18)
    assert(pozzettos.numCards()== 11)
    assert(pozzettos.firstPozzettoAvailable().size == 11)
    assert(pozzettos.numCards()== 0)
  }

  test("ask the pozzetto 3 times"){
    val list = List(
      PozzettoDeck.build(Deck.allRanksWithJollyCards().slice(0,18)),
      PozzettoDeck.build(Deck.allRanksWithJollyCards().slice(1,12))
    )
    val pozzettos = Pozzettos.build(list)
    assert(pozzettos.firstPozzettoAvailable().size == 18)
    assert(pozzettos.firstPozzettoAvailable().size == 11)
    assertThrows[NoSuchElementException]{
      pozzettos.firstPozzettoAvailable()
    }
  }

}
