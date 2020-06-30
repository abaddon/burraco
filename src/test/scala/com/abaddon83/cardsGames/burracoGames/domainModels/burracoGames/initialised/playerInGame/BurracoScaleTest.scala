package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGame

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.BurracoScale
import com.abaddon83.cardsGames.shares.decks.{Card, Ranks, Suits}
import org.scalatest.funsuite.AnyFunSuite

class BurracoScaleTest extends AnyFunSuite{

  test("given I have 5 cards with the same suit, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Nine),
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.King)
    assert(sortedScale.showCards()(2).rank == Ranks.Jack)
    assert(sortedScale.showCards.last.rank == Ranks.Nine)
  }

  test("given 5 cards from K to 8 with 9 missing, when I create a scale then I receive an error") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.King),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten),
      Card(Suits.Heart, Ranks.Eight),
    ))
    assertThrows[AssertionError]{
      BurracoScale(cards)
    }
  }
  test("given 6 cards from K to 8 with 9 missing and a Jolly, when I create a scale then I have a new scale") {
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
    assert(sortedScale.showCards()(2).rank == Ranks.Jack)
    assert(sortedScale.showCards()(4).rank == Ranks.Jolly)
    assert(sortedScale.showCards.last.rank == Ranks.Eight)
  }

  test("given 5 cards from 5 to 1, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace),
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Five)
    assert(sortedScale.showCards()(2).rank == Ranks.Three)
    assert(sortedScale.showCards()(3).rank == Ranks.Two)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 5 cards from 5 to 1 and a 2 with a different Suit, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Tile, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace),
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Five)
    assert(sortedScale.showCards()(2).rank == Ranks.Three)
    assert(sortedScale.showCards()(3).rank == Ranks.Two)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 7 cards from 7 to 1 with a card missing and a Jolly, when I create a scale then I have a new scale") {
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
    assert(sortedScale.showCards()(2).rank == Ranks.Five)
    assert(sortedScale.showCards()(4).rank == Ranks.Three)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 7 cards from 7 to 1 with a card missing and 2 Two, when I create a scale then I have a new scale") {
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
    assert(sortedScale.showCards()(2).rank == Ranks.Five)
    assert(sortedScale.showCards()(4).rank == Ranks.Three)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 7 cards from A to 8 with the card 9 missing and a Jolly, when I create a scale then I have a new scale") {
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
    assert(sortedScale.showCards()(2).rank == Ranks.Queen)
    assert(sortedScale.showCards()(4).rank == Ranks.Ten)
    assert(sortedScale.showCards.last.rank == Ranks.Eight)
  }

  test("given 5 cards from A to 10 with the card K missing and a Jolly, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Ace),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Ace)
    assert(sortedScale.showCards()(2).rank == Ranks.Queen)
    assert(sortedScale.showCards.last.rank == Ranks.Ten)
  }

  test("given 4 cards from A to 10 with the card K missing, when I create a scale then I receive an error") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Ace),
      Card(Suits.Heart, Ranks.Queen),
      Card(Suits.Heart, Ranks.Jack),
      Card(Suits.Heart, Ranks.Ten)
    ))
    assertThrows[AssertionError] {
      BurracoScale(cards)
    }
  }

  test("given 6 cards from 7 to A with the card 4 missing and with a card 2 with a different suit, when I create a scale then I receive an error") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Tile, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Ace),
    ))
    assertThrows[AssertionError]{
      BurracoScale(cards)
    }
  }

  test("given 5 cards from 5 to A with the card 3 missing and with 2 cards 2, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Tile, Ranks.Two),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Five)
    assert(sortedScale.showCards()(2).rank == Ranks.Two && sortedScale.showCards()(2).suit == Suits.Tile)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 5 cards from 5 to A with the card 4 missing and a Jolly, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Five)
    assert(sortedScale.showCards()(2).rank == Ranks.Jolly && sortedScale.showCards()(2).suit == Suits.Jolly)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 4 cards from K to 10 with no card missing and a Jolly, when I create a scale then I have a new scale") {
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

  test("given 5 cards from 5 to A with a Jolly, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Ace)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Jolly)
    assert(sortedScale.showCards()(2).rank == Ranks.Four)
    assert(sortedScale.showCards.last.rank == Ranks.Ace)
  }

  test("given 4 cards from 6 to 2 with a Jolly, when I create a scale then I have a new scale") {
    val cards = scala.util.Random.shuffle(List(
      Card(Suits.Jolly, Ranks.Jolly),
      Card(Suits.Heart, Ranks.Six),
      Card(Suits.Heart, Ranks.Five),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Two)
    ))
    val sortedScale = BurracoScale(cards)

    assert(sortedScale.showCards.head.rank == Ranks.Six)
    assert(sortedScale.showCards()(2).rank == Ranks.Four)
    assert(sortedScale.showCards.last.rank == Ranks.Two)
  }

  test("given a scale from 3 to 5, when I add a card 6 with the same suit, then the scale increase the size") {
    val cards = List(
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(Card(Suits.Heart, Ranks.Six))

    val actualScale=scale.addCards(cardToAdd)

    assert(actualScale.showCards().size == cards.size+1)
  }

  test("given a scale from 3 to 5, when I add a card 6 with the different suit, then the scale return an error") {
    val cards = List(
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(Card(Suits.Tile, Ranks.Six))

    assertThrows[AssertionError]{
      scale.addCards(cardToAdd)
    }

  }

  test("given a scale from 3 to 5, when I add a card Jolly, then the scale increase the size") {
    val cards = List(
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(Card(Suits.Jolly, Ranks.Jolly))

    val actualScale=scale.addCards(cardToAdd)

    assert(actualScale.showCards().size == cards.size+1)
  }

  test("given a scale from 3 to 5, when I add a card 7 with the the same suit, then the scale return an error") {
    val cards = List(
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(Card(Suits.Tile, Ranks.Six))

    assertThrows[AssertionError]{
      scale.addCards(cardToAdd)
    }
  }

  test("given a scale from 2 to 5, when I add a card 7 with the the same suit, then the scale increase the size") {
    val cards = List(
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(Card(Suits.Heart, Ranks.Seven))

    val actualScale=scale.addCards(cardToAdd)

    assert(actualScale.showCards().size == cards.size+1)
  }

  test("given a scale from 2 to 5, when I add a card 7 with the the different suit, then the scale fail") {
    val cards = List(
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(Card(Suits.Tile, Ranks.Six))

    assertThrows[AssertionError]{
      scale.addCards(cardToAdd)
    }
  }

  test("given a scale from 2 to 5, when I add 2 cards 7 and Jolly with the the same suit, then the scale increase the siz") {
    val cards = List(
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four),
      Card(Suits.Heart, Ranks.Five)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Jolly, Ranks.Jolly)
    )

    val actualScale=scale.addCards(cardToAdd)

    assert(actualScale.showCards().size == cards.size+2)
  }

  test("given a scale from 2 to 4, when I add 2 cards 7 and Jolly with the the same suit, then the scale return an error") {
    val cards = List(
      Card(Suits.Heart, Ranks.Two),
      Card(Suits.Heart, Ranks.Three),
      Card(Suits.Heart, Ranks.Four)
    )
    val scale = BurracoScale.apply(cards)
    val cardToAdd = List(
      Card(Suits.Heart, Ranks.Seven),
      Card(Suits.Jolly, Ranks.Jolly)
    )

    assertThrows[AssertionError]{
      scale.addCards(cardToAdd)
    }
  }

}




