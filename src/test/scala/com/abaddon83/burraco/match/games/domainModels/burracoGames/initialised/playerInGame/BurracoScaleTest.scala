package com.abaddon83.burraco.`match`.games.domainModels.initialised.playerInGame

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.BurracoScale
import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

class BurracoScaleTest extends AnyFunSuite{

  test("sort a valid scale") {
    val cards = List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Nine),
    )
    val sortedScale = BurracoScale(cards)
    assert(sortedScale.showCards.head.rank == Ranks.King)
    assert(sortedScale.showCards(2).rank == Ranks.Jack)
    assert(sortedScale.showCards.last.rank == Ranks.Nine)
  }

  test("sort a unsorted valid scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Nine),
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.King)
    assert(sortedScale.showCards(2).rank == Ranks.Jack)
    assert(sortedScale.showCards.last.rank == Ranks.Nine)
  }

  test("sort a unsorted scale with missing cards") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Eight),
    ))
    assertThrows[Exception]{
      BurracoScale(cards)
    }
  }

  test("sort a unsorted scale with missing cards and a Jolly") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Eight),
    ))
    val sortedScale = BurracoScale(cards)


    assert(sortedScale.showCards.head.rank == Ranks.King)
    assert(sortedScale.showCards(2).rank == Ranks.Jack)
    assert(sortedScale.showCards(4).rank == Ranks.Jolly)
    assert(sortedScale.showCards.last.rank == Ranks.Eight)
  }

  test("sort a unsorted scale with missing cards a Jolly and a Two as normal card") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace),
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Seven)
    assert(sortedScale.showCards(2).rank == Ranks.Five)
    assert(sortedScale.showCards(4).rank == Ranks.Three)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("sort a unsorted scale with missing cards a Two as Jolly and a Two as normal card") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Tile, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace),
    ))
    val sortedScale = BurracoScale(cards)


    assert(sortedScale.showCards.head.rank == Ranks.Seven)
    assert(sortedScale.showCards(2).rank == Ranks.Five)
    assert(sortedScale.showCards(4).rank == Ranks.Three)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("sort a unsorted scale with missing cards, a Jolly and an ACE On the head, first case") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Ace),
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Eight)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Ace)
    assert(sortedScale.showCards(2).rank == Ranks.Queen)
    assert(sortedScale.showCards(4).rank == Ranks.Ten)
    assert(sortedScale.showCards.last.rank == Ranks.Eight)
  }

  test("sort a unsorted scale with missing cards, a Jolly and an ACE On the head, second case") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Ace),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Ace)
    assert(sortedScale.showCards(2).rank == Ranks.Queen)
    assert(sortedScale.showCards.last.rank == Ranks.Ten)
  }

  test("sort a unsorted scale with missing cards, NO Jolly and an ACE On the head, should fail") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Ace),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten)
    ))
    assertThrows[Exception] {
      BurracoScale(cards)
    }
  }

  test("sort a unsorted scale with missing cards without a Two as Jolly and with a Two as normal card") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Tile, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Ace),
    ))
    assertThrows[Exception]{
      BurracoScale(cards)
    }
  }

  test("sort a unsorted scale from Five to Ace with a hole and 2 Two") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Tile, Ranks.Two),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace)
    ))
    val sortedScale = BurracoScale(cards)


    assert(sortedScale.showCards.head.rank == Ranks.Five)
    assert(sortedScale.showCards(2).rank == Ranks.Two && sortedScale.showCards(2).suit == Suits.Tile)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("sort a unsorted scale from Five to Ace with a hole and 1 Two and a jolly") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace)
    ))
    val sortedScale = BurracoScale(cards)



    assert(sortedScale.showCards.head.rank == Ranks.Five)
    assert(sortedScale.showCards(2).rank == Ranks.Jolly && sortedScale.showCards(2).suit == Suits.Jolly)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("sort a unsorted scale from Ace to Ace with a hole and 2 Two") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten)
    ))
    val sortedScale = BurracoScale(cards)

    assert(
      (sortedScale.showCards.head.rank == Ranks.Jolly && sortedScale.showCards.last.rank == Ranks.Ten) ||
        (sortedScale.showCards.head.rank == Ranks.King && sortedScale.showCards.last.rank == Ranks.Jolly)
    )

  }

  test("sort a unsorted scale from Five to Ace with no hole and 1 Two and a jolly") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace)
    ))
    val sortedScale = BurracoScale(cards)

    //debug(cards,sortedScale.showCards)

    assert(sortedScale.showCards.head.rank == Ranks.Jolly)
    assert(sortedScale.showCards(2).rank == Ranks.Four)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("sort a unsorted scale from Six to Two with a jolly") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Two)
    ))
    val sortedScale = BurracoScale(cards)

    //debug(cards,sortedScale.showCards)

    assert(sortedScale.showCards.head.rank == Ranks.Six)
    assert(sortedScale.showCards(2).rank == Ranks.Four)
    assert(sortedScale.showCards.last.rank == Ranks.Two)
  }



  private def debug(cards: List[Card], sortedCards: List[Card]) = {
    println("TEST DEBUG START")
    cards.foreach( card => println(card))
    println("---SORTED---")
    sortedCards.foreach( card => println(card))
    println("TEST DEBUG END")
  }

}
