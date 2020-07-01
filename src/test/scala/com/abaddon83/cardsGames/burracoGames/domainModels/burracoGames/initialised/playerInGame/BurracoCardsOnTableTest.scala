package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGame

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoCardsOnTable, BurracoScale, BurracoTris}
import com.abaddon83.cardsGames.shares.decks.{Card, Deck, Ranks, Suits}
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
    val expectedScaleCards = scale.numCards()

    assert(burracoCardsOnTable.burracoList().size == expectedSize)
    assert(burracoCardsOnTable.burracoList().head.getBurracoId() == expectedId)
    assert(burracoCardsOnTable.numCardsOnTable() == expectedTrisCards + expectedScaleCards)
  }

  test("given a tris and a scale on table when add a tris then the num of tris increase") {

    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(createTris(true)),
      listOfScale = List(createScale(false))
    )

    val trisToAdd = createTris(false)
    val actualBurracoCardsOnTable = burracoCardsOnTable.addTris(trisToAdd)

    val expectedListOfTris = 2
    val expectedListOScale = 1

    assert(actualBurracoCardsOnTable.showScale().size == expectedListOScale)
    assert(actualBurracoCardsOnTable.showTris().size == expectedListOfTris)

  }

  test("given a tris and a scale on table when add a scale then the num of scale increase") {

    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(createTris(true)),
      listOfScale = List(createScale(false))
    )

    val scaleToAdd = createScale(false)
    val actualBurracoCardsOnTable = burracoCardsOnTable.addScale(scaleToAdd)

    val expectedListOfTris = 1
    val expectedListOScale = 2

    assert(actualBurracoCardsOnTable.showScale().size == expectedListOScale)
    assert(actualBurracoCardsOnTable.showTris().size == expectedListOfTris)

  }

  test("given a tris Id on table when append a new card then the size of the tris increase") {

    val trisOnTable=createTris(true)

    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(trisOnTable,createTris(true)),
      listOfScale = List()
    )
    val cardsToAppend = List(Card(suit = Suits.Pike,rank = Ranks.Three))

    val actualBurracoCardsOnTable = burracoCardsOnTable.appendCardOnBurraco(trisOnTable.getBurracoId(),cardsToAppend)

    val expectedNumCardsTrisOnTable = trisOnTable.numCards()+1

    val actualNumCardsTrisOnTable = actualBurracoCardsOnTable.showTris().find(t => t.getBurracoId() ==trisOnTable.getBurracoId()).getOrElse { throw new Exception}.showCards().size

    assert(actualNumCardsTrisOnTable == expectedNumCardsTrisOnTable)

  }

  test("given a scale Id on table when append a new card then, the size of the scale increase") {

    val scaleOnTable=createScale(true)

    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(),
      listOfScale = List(scaleOnTable,createScale(true))
    )
    val cardsToAppend = List(Card(suit = Suits.Heart,rank = Ranks.Nine))

    val actualBurracoCardsOnTable = burracoCardsOnTable.appendCardOnBurraco(scaleOnTable.getBurracoId(),cardsToAppend)

    val expectedNumCardsScaleOnTable = scaleOnTable.numCards()+1

    val actualNumCardsScaleOnTable = actualBurracoCardsOnTable.showScale().find(t => t.getBurracoId() ==scaleOnTable.getBurracoId()).getOrElse { throw new Exception}.showCards().size

    assert(actualNumCardsScaleOnTable == expectedNumCardsScaleOnTable)

  }

  test("given a scale Id not available, when append a new card then, I receive an error") {

    val scaleOnTable=createScale(true)

    val burracoCardsOnTable = BurracoCardsOnTable(
      listOfTris = List(),
      listOfScale = List(createScale(true))
    )
    val cardsToAppend = List(Card(suit = Suits.Heart,rank = Ranks.Nine))


    assertThrows[NoSuchElementException]{
      burracoCardsOnTable.appendCardOnBurraco(scaleOnTable.getBurracoId(),cardsToAppend)
    }

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
