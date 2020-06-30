package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGame

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.MazzettoDeck
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.cardsGames.shares.decks.{Card, Deck, Ranks, Suits}
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class PlayerInGameTest extends AnyFunSuite {

  test("given a Mazzetto, when I take it  for the first time, then I have it on my cards") {

    val myCards= List(Card(Suits.Heart,Ranks.Six))
    val playerInGame = PlayerInGameTestFactory().withCards(myCards).build()
    val mazzetto: MazzettoDeck = MazzettoDeck.build(Deck.allRanksCards().take(11))

    val expectedMyCardsSize = mazzetto.numCards() + myCards.size
    val expectedMazzettoTaken = true
    val expectedAllCardsSize = myCards.size + mazzetto.numCards()

    val actualPlayerInGame = playerInGame.pickUpMazzetto(mazzetto)

    assert(actualPlayerInGame.mazzettoTaken == expectedMazzettoTaken)
    assert(actualPlayerInGame.showMyCards().size == expectedMyCardsSize)
    assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)
  }

  test("given a Mazzetto, when I take it  for the second time, then I receive an error") {
    val myCards= List(Card(Suits.Heart,Ranks.Six))
    val playerInGame = PlayerInGameTestFactory().withCards(myCards).build()
    val mazzetto: MazzettoDeck = MazzettoDeck.build(Deck.allRanksCards().take(11))
    val mazzetto2: MazzettoDeck = MazzettoDeck.build(Deck.allRanksCards().take(11))

    assertThrows[AssertionError]{
      playerInGame.pickUpMazzetto(mazzetto).pickUpMazzetto(mazzetto2)
    }
  }

  test("given a Tris on My cards, when I drop it, then I see it on the table"){
    val trisCards = Deck.allRanksCards().filter(c => c.rank == Ranks.Seven).take(4)
    val myCards= Deck.allRanksCards().take(5) ++ trisCards
    val playerInGame = PlayerInGameTestFactory().withCards(myCards).build()
    val trisToDrop = BurracoTris(trisCards)

    val expectedMyCardsSize = 5
    val expectedTrisOnTable = trisToDrop
    val expectedAllCardsSize = myCards.size

    val actualPlayerInGame = playerInGame.dropATris(trisToDrop)

    assert(actualPlayerInGame.showMyCards().size == expectedMyCardsSize)
    assert(actualPlayerInGame.showTrisOnTable().head == expectedTrisOnTable)
    assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)

  }

  test("given a Scale on My cards, when I drop it, then I see it on the table"){
    val scaleCards = List(
      Card(Suits.Heart,Ranks.Four),
      Card(Suits.Heart,Ranks.Five),
      Card(Suits.Heart,Ranks.Six),
      Card(Suits.Heart,Ranks.Seven)
    )
    val myCards= Deck.allRanksCards().take(5) ++ scaleCards
    val playerInGame = PlayerInGameTestFactory().withCards(myCards).build()
    val scaleToDrop = BurracoScale(scaleCards)

    val expectedMyCardsSize = 5
    val expectedScaleOnTable = scaleToDrop
    val expectedAllCardsSize = myCards.size

    val actualPlayerInGame = playerInGame.dropAScale(scaleToDrop)

    assert(actualPlayerInGame.showMyCards().size == expectedMyCardsSize)
    assert(actualPlayerInGame.showScalesOnTable().head == expectedScaleOnTable)
    assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)

  }

  test("given some cards, when I drop one of them, then I have a card less on my hand"){
    val cardToDrop = Deck.allRanksCards().take(1)
    val myCards= Deck.allRanksCards().filterNot( p=> p == cardToDrop).take(5) ++ cardToDrop
    val playerInGame = PlayerInGameTestFactory().withCards(myCards).build()

    val expectedMyCardSize = myCards.size-1

    val actualPlayerInGame = playerInGame.dropACard(cardToDrop.head)
    val expectedCardDropped = cardToDrop
    val expectedAllCardsSize = myCards.size-1

    assert(actualPlayerInGame.showMyCards().size == expectedMyCardSize)
    assert(!actualPlayerInGame.showMyCards().contains(expectedCardDropped))
    assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)
  }

  test("given a card to append on a Scale on table, when I append the card, then the card is on the burraco"){
    val cardsScale = List(
      Card(Suits.Heart,Ranks.Four),
      Card(Suits.Heart,Ranks.Five),
      Card(Suits.Heart,Ranks.Six),
      Card(Suits.Heart,Ranks.Seven)
    )
    println(s"cardsScale: ${cardsScale.size}")
    val cardToDrop = Card(Suits.Heart,Ranks.Eight)
    var deck = (Deck.allRanksCards() diff cardsScale) diff List(cardToDrop)
    val myCards= deck.take(5) ++ List(cardToDrop)
    deck = deck diff myCards
    println(s"myCards: ${myCards.size}")
    val burracoScale = BurracoScale(cardsScale)
    val burracoTris = BurracoTris(deck.filter(c => c.rank == Ranks.Seven).take(4))
    deck = deck diff burracoTris.showCards()
    println(s"burracoTris: ${burracoTris.numCards()}")
    val playerInGame = PlayerInGameTestFactory()
      .withCards(myCards)
      .withScalaOnTable(burracoScale)
      .withTrisOnTable(burracoTris)
      .build()
println(s"totalPlayerCards(): ${playerInGame.totalPlayerCards()}")
    val expectedScaleSize = burracoScale.showCards().size +1
    val expectedMyCardsSize = myCards.size -1
    val expectedAllCardsSize = myCards.size + burracoScale.showCards().size + burracoTris.showCards().size

    val actualPlayerInGame = playerInGame.appendACardOnBurracoDropped(burracoScale.getBurracoId(),List(cardToDrop))

    println(s"totalPlayerCards() later: ${actualPlayerInGame.totalPlayerCards()}")

    assert(actualPlayerInGame.showScalesOnTable().head.showCards().contains(cardToDrop))
    assert(actualPlayerInGame.showScalesOnTable().head.showCards().size == expectedScaleSize)
    assert(actualPlayerInGame.showScalesOnTable().head.showCards().size == expectedMyCardsSize)
    assert(actualPlayerInGame.totalPlayerCards() == expectedAllCardsSize)
  }




}

object PlayerInGameTestFactory {
  def apply(): PlayerInGameTestFactory = {
    apply(PlayerIdentity())
  }

  def apply(playerIdentity: PlayerIdentity): PlayerInGameTestFactory = {
    PlayerInGameTestFactory(
      playerInGame = PlayerInGame.build(playerIdentity, List[Card]().empty)
    )
  }

}

case class PlayerInGameTestFactory(private val playerInGame: PlayerInGame) {

  def withCards(cards: List[Card]): PlayerInGameTestFactory = {
    this.copy(
      playerInGame = playerInGame.copy(
        cards = cards
      )
    )
  }

  def withTrisOnTable(burracoTris: BurracoTris): PlayerInGameTestFactory = {
    this.copy(
      playerInGame = playerInGame.dropATris(burracoTris)
    )
  }

  def withScalaOnTable(burracoScale: BurracoScale): PlayerInGameTestFactory = {
    this.copy(
      playerInGame = playerInGame.dropAScale(burracoScale)
    )
  }

  def build(): PlayerInGame = playerInGame


}
