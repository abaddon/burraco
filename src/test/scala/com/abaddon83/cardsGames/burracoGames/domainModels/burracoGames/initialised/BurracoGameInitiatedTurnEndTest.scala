package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.completed.BurracoGameCompleted
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoCardsOnTable, BurracoTris, PlayerInGame}
import com.abaddon83.cardsGames.mocks.BurracoGameInitTurnTestFactory
import com.abaddon83.cardsGames.shares.decks.{Card, Ranks, Suits}
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitiatedTurnEndTest extends AnyFunSuite{

  test("Given a player with 0 cards, when pickup the mazzetto, then receive the mazzetto cards") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    val actualGame = game.pickupMazzetto(player1Id)

    assert(actualGame.playerCards(player1Id).size == 11)
    assert(actualGame.listOfPlayers.size == 2)
  }

  test("Given a player with a card, when pickup the mazzetto, then receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(Card(Suits.Heart,Ranks.Six)), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    assertThrows[AssertionError]{
      game.pickupMazzetto(player1Id)
    }
  }

  test("Given a player with 0 cards and the pozzetto already taken, when pickup the mazzetto, then receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(), List()), true)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    assertThrows[AssertionError]{
      game.pickupMazzetto(player1Id)
    }
  }

  test("Given a player of another turn, when pickup the mazzetto, then receive an error") {
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseEnd()

    assertThrows[UnsupportedOperationException]{
      game.pickupMazzetto(player1Id)
    }
  }

  test("Given a player of another game, when pickup the mazzetto, then receive an error") {
    val game = BurracoGameInitTurnTestFactory()
      .buildTurnPhaseEnd()

    assertThrows[NoSuchElementException]{
      game.pickupMazzetto(PlayerIdentity())
    }
  }

  test("Given a player than finished the turn, when confirm the turn is over, then it's the turn of the next player") {
    val player1Id = PlayerIdentity()
    val player2Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id, player2Id = player2Id)
      .buildTurnPhaseEnd()

    val actualGame = game.nextPlayerTurn(player1Id)
    assert(actualGame.validatePlayerTurn(player2Id) == player2Id)
    assert(actualGame.isInstanceOf[BurracoGameInitiatedTurnStart])
  }

  test("Given a player during the turn of another player, when confirm the turn is over, then it's the turn of the next player") {
    val player1Id = PlayerIdentity()
    val player2Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id, player2Id = player2Id)
      .setPlayer2Turn()
      .buildTurnPhaseEnd()

    assertThrows[UnsupportedOperationException]{
      game.nextPlayerTurn(player1Id)
    }
  }

  test("Given a player of another game, when confirm the turn is over, then it's the turn of the next player") {
    val player1Id = PlayerIdentity()
    val player2Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id, player2Id = player2Id)
      .setPlayer2Turn()
      .buildTurnPhaseEnd()

    assertThrows[NoSuchElementException]{
      game.nextPlayerTurn(PlayerIdentity())
    }
  }

  test("Given a player with a Burraco, no cards, when complete the turn, then the game is end"){
    val player1Id = PlayerIdentity()
    val tris = BurracoTris(List(
      Card(Suits.Heart,Ranks.Six),Card(Suits.Heart,Ranks.Six),
      Card(Suits.Pike,Ranks.Six),Card(Suits.Pike,Ranks.Six),
      Card(Suits.Tile,Ranks.Six),Card(Suits.Tile,Ranks.Six),
      Card(Suits.Clover,Ranks.Six),Card(Suits.Clover,Ranks.Six)
    ))
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(tris), List()), true)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    val actualGame = game.completeGame(player1Id)

    assert(actualGame.isInstanceOf[BurracoGameCompleted])
  }

  test("Given a player with a Burraco, some cards, when complete the turn, then receive an error"){
    val player1Id = PlayerIdentity()
    val tris = BurracoTris(List(
      Card(Suits.Heart,Ranks.Six),Card(Suits.Heart,Ranks.Six),
      Card(Suits.Pike,Ranks.Six),Card(Suits.Pike,Ranks.Six),
      Card(Suits.Tile,Ranks.Six),Card(Suits.Tile,Ranks.Six),
      Card(Suits.Clover,Ranks.Six),Card(Suits.Clover,Ranks.Six)
    ))
    val player1 = PlayerInGame(player1Id, List(Card(Suits.Heart,Ranks.Four)), BurracoCardsOnTable(List(tris), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    assertThrows[AssertionError]{
      game.completeGame(player1Id)
    }
  }

  test("Given a player with the mazzetto didn't take, when complete the turn, then receive an error"){
    val player1Id = PlayerIdentity()
    val tris = BurracoTris(List(
      Card(Suits.Heart,Ranks.Six),Card(Suits.Heart,Ranks.Six),
      Card(Suits.Pike,Ranks.Six),Card(Suits.Pike,Ranks.Six),
      Card(Suits.Tile,Ranks.Six),Card(Suits.Tile,Ranks.Six),
      Card(Suits.Clover,Ranks.Six),Card(Suits.Clover,Ranks.Six)
    ))
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(tris), List()), false)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    assertThrows[AssertionError]{
      game.completeGame(player1Id)
    }
  }

  test("Given a player with not a burraco, when complete the turn, then receive an error"){
    val player1Id = PlayerIdentity()
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(), List()), true)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .buildTurnPhaseEnd()

    assertThrows[AssertionError]{
      game.completeGame(player1Id)
    }
  }

  test("Given a player during the turn of another player, when complete the turn, then receive an error"){
    val player1Id = PlayerIdentity()
    val tris = BurracoTris(List(
      Card(Suits.Heart,Ranks.Six),Card(Suits.Heart,Ranks.Six),
      Card(Suits.Pike,Ranks.Six),Card(Suits.Pike,Ranks.Six),
      Card(Suits.Tile,Ranks.Six),Card(Suits.Tile,Ranks.Six),
      Card(Suits.Clover,Ranks.Six),Card(Suits.Clover,Ranks.Six)
    ))
    val player1 = PlayerInGame(player1Id, List(), BurracoCardsOnTable(List(tris), List()), true)
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer1(player1)
      .setPlayer2Turn()
      .buildTurnPhaseEnd()

    assertThrows[UnsupportedOperationException]{
      game.completeGame(player1Id)
    }
  }

  test("Given a player of another game, when complete the turn, then receive an error"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .buildTurnPhaseEnd()

    assertThrows[NoSuchElementException]{
      game.completeGame(PlayerIdentity())
    }
  }

}
