package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

class ScaleTest extends AnyFunSuite{

  test("sort a valid scale") {
    val cards = List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Nine),
    )
    val sortedCards = Scale.sortScale(cards,Suits.Heart)
    assert(sortedCards.head.rank == Ranks.King)
    assert(sortedCards(2).rank == Ranks.Jack)
    assert(sortedCards.last.rank == Ranks.Nine)
  }

  test("sort a unsorted valid scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Nine),
    ))
    val sortedCards = Scale.sortScale(cards,Suits.Heart)

    assert(sortedCards.head.rank == Ranks.King)
    assert(sortedCards(2).rank == Ranks.Jack)
    assert(sortedCards.last.rank == Ranks.Nine)
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
      Scale.sortScale(cards,Suits.Heart)
    }
  }

  test("sort a unsorted scale with missing cards and a Jolly") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Eight),
    ))
    val sortedCards = Scale.sortScale(cards,Suits.Heart)

   //debug(cards,sortedCards)

    assert(sortedCards.head.rank == Ranks.King)
    assert(sortedCards(2).rank == Ranks.Jack)
    assert(sortedCards(4).rank == Ranks.Jolly)
    assert(sortedCards.last.rank == Ranks.Eight)
  }

  test("sort a unsorted scale with missing cards a Jolly and a Two as normal card") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace),
    ))
    val sortedCards = Scale.sortScale(cards,Suits.Heart)

    //debug(cards,sortedCards)

    assert(sortedCards.head.rank == Ranks.Seven)
    assert(sortedCards(2).rank == Ranks.Five)
    assert(sortedCards(4).rank == Ranks.Three)
    assert(sortedCards.last.rank == Ranks.Ace)
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
    val sortedCards = Scale.sortScale(cards,Suits.Heart)

    debug(cards,sortedCards)

    assert(sortedCards.head.rank == Ranks.Seven)
    assert(sortedCards(2).rank == Ranks.Five)
    assert(sortedCards(4).rank == Ranks.Three)
    assert(sortedCards.last.rank == Ranks.Ace)
  }




  private def debug(cards: List[Card], sortedCards: List[Card]) = {
    println("TEST DEBUG START")
    cards.foreach( card => println(card))
    println("---SORTED---")
    sortedCards.foreach( card => println(card))
    println("TEST DEBUG END")
  }

}
