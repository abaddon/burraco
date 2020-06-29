package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGame

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.BurracoTris
import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.{Card, Deck, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

class BurracoTrisTest extends AnyFunSuite{
  test("given I have 3 cards with the same rank, when I create a tris then I have a new tris") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3
    val cards = Deck.allRanksWithJollyCards().filter(c => c.rank == burracoTrisRank).take(burracoTrisSize)

    val actualBurracoTris = BurracoTris(cards)

    assert(actualBurracoTris.showCards().size == burracoTrisSize)
    assert(actualBurracoTris.showRank() == burracoTrisRank)
  }

  test("given I have 2 cards with the same rank, when I create a tris then I receive an error") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 2
    val cards = Deck.allRanksWithJollyCards().filter(c => c.rank == burracoTrisRank).take(burracoTrisSize)

    assertThrows[AssertionError]{
      BurracoTris(cards)
    }
  }

  test("given I have 3 cards with the different rank, when I create a tris then I receive an error") {

    val cards = List(
      Card(suit = Suits.Heart,rank = Ranks.Eight),
      Card(suit = Suits.Heart,rank = Ranks.Eight),
      Card(suit = Suits.Heart,rank = Ranks.Three)
    )

    assertThrows[AssertionError] {
      BurracoTris(cards)
    }
  }

  test("given I have 3 cards with the same rank and a jolly, when I create a tris then I have a new tris") {
    val burracoTrisRank = Ranks.Five
    val cards = List(
      Card(suit = Suits.Heart,rank = burracoTrisRank),
      Card(suit = Suits.Heart,rank = burracoTrisRank),
      Card(suit = Suits.Jolly,rank = Ranks.Jolly)
    )
    val tris=BurracoTris(cards)

    assert(tris.showCards.size == cards.size)
    assert(tris.showRank() == burracoTrisRank)



  }

  test("given I have 3 cards with the same rank and a Two, when I create a tris then I have a new tris") {
    val burracoTrisRank = Ranks.Five
    val cards = List(
      Card(suit = Suits.Heart,rank = burracoTrisRank),
      Card(suit = Suits.Heart,rank = burracoTrisRank),
      Card(suit = Suits.Heart,rank = Ranks.Two)
    )

    val tris=BurracoTris(cards)

    assert(tris.showCards.size == cards.size)
    assert(tris.showRank() == burracoTrisRank)

  }

  test("given I have 3 Jolly, when I create a tris then  I receive an error") {
    val burracoTrisRank = Ranks.Jolly
    val cards = List(
      Card(suit = Suits.Jolly,rank = burracoTrisRank),
      Card(suit = Suits.Jolly,rank = burracoTrisRank),
      Card(suit = Suits.Jolly,rank = burracoTrisRank)
    )

    assertThrows[AssertionError]{
      BurracoTris(cards)
    }

  }

  test("given I have 3 Two, when I create a tris then I receive an error") {
    val burracoTrisRank = Ranks.Two
    val cards = List(
      Card(suit = Suits.Heart,rank = burracoTrisRank),
      Card(suit = Suits.Pike,rank = burracoTrisRank),
      Card(suit = Suits.Tile,rank = burracoTrisRank)
    )

    assertThrows[AssertionError]{
      BurracoTris(cards)
    }

  }

  test("given a tris, when I add a card with the same rank, then the tris size is increased by one") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Heart,rank = burracoTrisRank))
    val burracoTris = createABurracoTrisWith(burracoTrisRank,burracoTrisSize)

    val actualBurracoTris=burracoTris.addCards(cards)

    assert(actualBurracoTris.showCards.size == burracoTrisSize+1)
    assert(actualBurracoTris.showCards.contains(cards(0)))
  }

  test("given a tris, when I add a Jolly, then the tris size is increased by one") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Jolly,rank = Ranks.Jolly))
    val burracoTris = createABurracoTrisWith(burracoTrisRank,burracoTrisSize)

    val actualBurracoTris=burracoTris.addCards(cards)

    assert(actualBurracoTris.showCards.size == burracoTrisSize+1)
    assert(actualBurracoTris.showCards.contains(cards(0)))
  }

  test("given a tris with a Jolly, when I add a Jolly, then I receive an error") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Jolly,rank = Ranks.Jolly))
    val burracoTris = createABurracoTrisWithAJollyAnd(burracoTrisRank,burracoTrisSize)

    assertThrows[AssertionError]{
      burracoTris.addCards(cards)
    }
  }

  test("given a tris with a Jolly, when I add a card with rank Two, then I receive an error") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Heart,rank = Ranks.Two))
    val burracoTris = createABurracoTrisWithAJollyAnd(burracoTrisRank,burracoTrisSize)

    assertThrows[AssertionError]{
      burracoTris.addCards(cards)
    }
  }

  test("given a tris that include a card with rank Two, when I add a Jolly, then I receive an error") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Jolly,rank = Ranks.Jolly))
    val burracoTris = createABurracoTrisWithATwoAnd(burracoTrisRank,burracoTrisSize)

    assertThrows[AssertionError]{
      burracoTris.addCards(cards)
    }
  }

  test("given a tris that include a card with rank Two, when I add a card with rank Two, then I receive an error") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Heart,rank = Ranks.Two))
    val burracoTris = createABurracoTrisWithATwoAnd(burracoTrisRank,burracoTrisSize)

    assertThrows[AssertionError]{
      burracoTris.addCards(cards)
    }
  }

  test("given a tris, when I add a card with a different rank, then I receive an error") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Heart,rank = Ranks.Three))
    val burracoTris = createABurracoTrisWith(burracoTrisRank,burracoTrisSize)

    assertThrows[AssertionError]{
      burracoTris.addCards(cards)
    }
  }

  //TODO missing test related to the definition of a Burraco

  private def createABurracoTrisWith(rank: Rank,numCards: Int): BurracoTris ={
    BurracoTris(
      cards = Deck.allRanksWithJollyCards().filter(c => c.rank == rank).take(numCards)
    )
  }

  private def createABurracoTrisWithAJollyAnd(rank: Rank,numCards: Int): BurracoTris ={
    BurracoTris(
      cards = Deck.allRanksWithJollyCards().filter(c => c.rank == rank).take(numCards-1) ++ List(Card(suit = Suits.Jolly,rank = Ranks.Jolly))
    )
  }

  private def createABurracoTrisWithATwoAnd(rank: Rank,numCards: Int): BurracoTris ={
    BurracoTris(
      cards = Deck.allRanksWithJollyCards().filter(c => c.rank == rank).take(numCards-1) ++ List(Card(suit = Suits.Heart,rank = Ranks.Two))
    )
  }



}
