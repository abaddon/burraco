package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.runnings.playerInGame

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoCardsOnTable, BurracoScale, BurracoTris}
import com.abaddon83.burraco.shares.decks.{Deck, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

class BurracoCardsOnTableTest extends AnyFunSuite {

  test("given a tris not yet burraco and a scala not yet burraco when I ask the list of burraco, then I receive an empty list") {
    val tris= createTris(false)
    val scale = createScale(false)
    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(tris),
      listOfScale = List(scale)
    )

    //println(s"tris: ${tris.getBurracoId()} - ${tris.showCards().size} - ${tris.isBurraco()}")
    //println(s"scale: ${scale.getBurracoId()} - ${scale.showCards().size} - ${scale.isBurraco()}")

    val expectedSize = 0

    assert(burracoCardsOnTable.burracoList().size == expectedSize)
  }

  test("given a tris burraco and a scala not yet burraco when I ask the list of burraco, then I receive a list with a burraco ") {
    val tris= createTris(true)
    val scale = createScale(false)
    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(tris),
      listOfScale = List(scale)
    )

    //println(s"tris: ${tris.getBurracoId()} - ${tris.showCards().size} - ${tris.isBurraco()}")
    //println(s"scale: ${scale.getBurracoId()} - ${scale.showCards().size} - ${scale.isBurraco()}")

    val expectedSize = 1
    val expectedId = tris.getBurracoId()
    val expectedTrisCards = tris.numCards()
    val expectedScaleCards = tris.numCards()

    assert(burracoCardsOnTable.burracoList().size == expectedSize)
    assert(burracoCardsOnTable.burracoList().head.getBurracoId() == expectedId)
    assert(burracoCardsOnTable.numCardsOnTable() == expectedTrisCards + expectedScaleCards)
  }

  test("given a") {

  }




  private def createScale(isBurraco: Boolean): BurracoScale = {
    BurracoScale(
      cards = isBurraco match {
        case true => (Deck.allRanksCards()++Deck.allRanksCards()).filter(c=>c.suit == Suits.Heart).sorted.take(7)
        case false => (Deck.allRanksCards()++Deck.allRanksCards()).filter(c=>c.suit == Suits.Heart).sorted.take(6)
      }
    )
  }

  private def createTris(isBurraco: Boolean): BurracoTris = {
    BurracoTris(
      cards = isBurraco match {
        case true => (Deck.allRanksCards()++Deck.allRanksCards()).filter(c=>c.rank == Ranks.Three).take(7)
        case false => (Deck.allRanksCards()++Deck.allRanksCards()).filter(c=>c.rank == Ranks.Queen).take(6)
      }
    )
  }

}
