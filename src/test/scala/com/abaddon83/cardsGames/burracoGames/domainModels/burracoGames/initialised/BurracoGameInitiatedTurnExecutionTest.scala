package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoId
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoCardsOnTable, BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.cardsGames.mocks.BurracoGameInitTurnTestFactory
import com.abaddon83.cardsGames.shares.decks.Ranks._
import com.abaddon83.cardsGames.shares.decks.{Card, Ranks, Suits}
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitiatedTurnExecutionTest extends AnyFunSuite {


  test("Given a player in game, when update the cards order, the operation is executed") {
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .buildTurnPhaseExecution()

    val playerCards = game.playerCards(player1Id)
    val orderedCards = playerCards.sorted

    val actualGame = game.updatePlayerCardsOrder(player1Id, orderedCards)

    assert(actualGame.playerCards(player1Id) == orderedCards)
    assert(actualGame.playerCards(player1Id) != playerCards)
  }

  test("Given a player not in game, when update the cards order, I receive an error") {
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id).buildTurnPhaseExecution()
    val playerCards = game.playerCards(player1Id)
    val orderedCards = playerCards.sorted

    assertThrows[NoSuchElementException] {
      game.updatePlayerCardsOrder(PlayerIdentity(), orderedCards)
    }
  }


  test("Given a player during his turn, when drop a tris, the operation is executed") {
    val tris = BurracoTris(BurracoId(), Ace, List(Card(Suits.Heart, Ace), Card(Suits.Heart, Ace), Card(Suits.Clover, Ace)))
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, tris.showCards(), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    val gameActual = game.dropOnTableATris(player1Id, tris)

    assert(gameActual.playerTrisOnTable(player1Id).size == 1)
    assert(gameActual.playerCards(player1Id).isEmpty)

  }

  test("Given a player not in the turn, when drop a tris, I receive an error") {
    val tris = BurracoTris(BurracoId(), Ace, List(Card(Suits.Heart, Ace), Card(Suits.Heart, Ace), Card(Suits.Clover, Ace)))
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, tris.showCards(), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException] {
      game.dropOnTableATris(player1Id, tris)
    }
  }

  test("Given a player not in this game, when drop a tris, I receive an error") {
    val tris = BurracoTris(BurracoId(), Ace, List(Card(Suits.Heart, Ace), Card(Suits.Heart, Ace), Card(Suits.Clover, Ace)))
    val game = BurracoGameInitTurnTestFactory()
      .buildTurnPhaseExecution()

    assertThrows[NoSuchElementException] {
      game.dropOnTableATris(PlayerIdentity(), tris)
    }
  }

  test("Given a player during his turn, when drop a scale, the operation is executed") {
    val scale = BurracoScale(List(
      Card(Suits.Heart,Ranks.Eight),
      Card(Suits.Heart,Ranks.Seven),
      Card(Suits.Heart,Ranks.Six),
      Card(Suits.Heart,Ranks.Five)
    ))
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, scale.showCards(), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    val gameActual = game.dropOnTableAScale(player1Id, scale)

    assert(gameActual.playerScalesOnTable(player1Id).size == 1)
    assert(gameActual.playerCards(player1Id).isEmpty)

  }

  test("Given a player not in the turn, when drop a scale, I receive an error") {
    val scale = BurracoScale(List(
      Card(Suits.Heart,Ranks.Eight),
      Card(Suits.Heart,Ranks.Seven),
      Card(Suits.Heart,Ranks.Six),
      Card(Suits.Heart,Ranks.Five)
    ))
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, scale.showCards(), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException] {
      game.dropOnTableAScale(player1Id, scale)
    }
  }

  test("Given a player not in this game, when drop a scale, I receive an error") {
    val scale = BurracoScale(List(
      Card(Suits.Heart,Ranks.Eight),
      Card(Suits.Heart,Ranks.Seven),
      Card(Suits.Heart,Ranks.Six),
      Card(Suits.Heart,Ranks.Five)
    ))
    val game = BurracoGameInitTurnTestFactory()
      .buildTurnPhaseExecution()

    assertThrows[NoSuchElementException] {
      game.dropOnTableAScale(PlayerIdentity(), scale)
    }
  }

  test("Given a player during his turn with a tris, when append a card on the tris, the operation is executed") {
    val tris = BurracoTris(BurracoId(), Ace, List(Card(Suits.Heart, Ace), Card(Suits.Heart, Ace), Card(Suits.Clover, Ace)))
    val player1Id = PlayerIdentity()
    val cardToAppend = List(Card(Suits.Tile, Ace))
    val player1 = PlayerInGame(player1Id, cardToAppend, BurracoCardsOnTable(listOfTris = List(tris),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseExecution()


    val exceptedTrisSize = tris.numCards() + cardToAppend.size

    val gameActual = game.appendCardsOnABurracoDropped(player1Id, cardToAppend,tris.getBurracoId())

    assert(gameActual.playerTrisOnTable(player1Id).size == 1)
    assert(gameActual.playerTrisOnTable(player1Id).head.numCards() == exceptedTrisSize)
    assert(gameActual.playerCards(player1Id).isEmpty)

  }

  test("Given a player with a tris and during the turn of another player, when append a card on the tris, I receive an error") {
    val tris = BurracoTris(BurracoId(), Ace, List(Card(Suits.Heart, Ace), Card(Suits.Heart, Ace), Card(Suits.Clover, Ace)))
    val player1Id = PlayerIdentity()
    val cardToAppend = List(Card(Suits.Tile, Ace))
    val player1 = PlayerInGame(player1Id, cardToAppend, BurracoCardsOnTable(listOfTris = List(tris),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException] {
      game.appendCardsOnABurracoDropped(player1Id, cardToAppend,tris.getBurracoId())
    }

  }

  test("Given a player of another game, with a tris, when append a card on the tris, I receive an error") {
    val tris = BurracoTris(BurracoId(), Ace, List(Card(Suits.Heart, Ace), Card(Suits.Heart, Ace), Card(Suits.Clover, Ace)))
    val player1Id = PlayerIdentity()
    val cardToAppend = List(Card(Suits.Tile, Ace))
    val player1 = PlayerInGame(player1Id, cardToAppend, BurracoCardsOnTable(listOfTris = List(tris),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    assertThrows[NoSuchElementException] {
      game.appendCardsOnABurracoDropped(PlayerIdentity(), cardToAppend,tris.getBurracoId())
    }
  }


  test("Given a player during his turn with no cards, when pickUp the mazzetto, then I can see the mazzetto cards on my hand") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    val actualGame = game.pickupMazzetto(player1Id)
    assert(actualGame.playerCards(player1Id).size == 11)

  }

  test("Given a player during his turn with some cards, when pickUp the mazzetto, then I receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(Card(Suits.Heart, Ace)), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException]{
      game.pickupMazzetto(player1Id)
    }
  }

  test("Given a player during his turn with no cards and a mazzetto already taken, when pickUp the mazzetto, then I receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(Card(Suits.Heart, Ace)), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), true)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException]{
      game.pickupMazzetto(player1Id)
    }
  }

  test("Given a player with no cards, during the tun of another player , when pickUp the mazzetto, then I receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException]{
      game.pickupMazzetto(player1Id)
    }
  }

  test("Given a player of another game , when pickUp the mazzetto, then I receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseExecution()

    assertThrows[NoSuchElementException]{
      game.pickupMazzetto(PlayerIdentity())
    }
  }

  test("Given a player during his turn with some cards, when drop a card on a discard pile, then I have a card less") {
    val player1Id = PlayerIdentity()
    val cardToDrop = Card(Suits.Heart,Ranks.Six)
    val player1 = PlayerInGame(player1Id, List(cardToDrop), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseExecution()

    val expectedDiscardPileSize = 1

    val actualGame = game.dropCardOnDiscardPile(player1Id,cardToDrop)
    assert(actualGame.playerCards(player1Id).isEmpty)
    assert(actualGame.showDiscardPile().size == expectedDiscardPileSize)
    assert(actualGame.showDiscardPile().contains(cardToDrop))

  }

  test("Given a player during the turn of another player, when drop a card on a discard pile, then receive an error") {
    val player1Id = PlayerIdentity()
    val cardToDrop = Card(Suits.Heart,Ranks.Six)
    val player1 = PlayerInGame(player1Id, List(cardToDrop), BurracoCardsOnTable(listOfTris = List(),listOfScale =  List()), false)
    val game = BurracoGameInitTurnTestFactory()
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseExecution()

    assertThrows[UnsupportedOperationException]{
      game.dropCardOnDiscardPile(player1Id,cardToDrop)
    }

  }

}






