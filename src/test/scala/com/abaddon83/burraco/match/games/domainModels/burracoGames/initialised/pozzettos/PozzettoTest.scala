package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.Deck
import org.scalatest.funsuite.AnyFunSuite

class PozzettoTest extends AnyFunSuite{
  test("add 2 pozzettos"){
    val list = List(
      PozzettoDeck.build(Deck.allRanksWithJollyCards().drop(11)) ,
      PozzettoDeck.build(Deck.allRanksWithJollyCards().drop(11))
    )
    val pozzettos = Pozzettos.build(list)
    assert(pozzettos.firstPozzettoAvailable() == list.head)
    assert(pozzettos.firstPozzettoAvailable() == list.last)
  }

}
