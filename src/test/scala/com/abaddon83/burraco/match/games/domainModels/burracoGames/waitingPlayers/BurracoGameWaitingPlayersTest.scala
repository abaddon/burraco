package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.{BurracoCardsDealt, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.`match`.games.domainModels.{PlayerNotAssigned, burracoGames}
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.mocks.BurracoGameInitTurnTestFactory
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameWaitingPlayersTest extends AnyFunSuite {


  test("Given a burraco game, when I add a new player, then the num of player increase") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()

    val newGame = game.addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assert(newGame.gameIdentity == game.gameIdentity)
    assert(newGame.numPlayers == game.numPlayers + 1)
  }

  test("Given a burraco game, when I add a player 2 times, then I receive an error") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()
    val player1 = PlayerNotAssigned(PlayerIdentity())
    val newGame = game.addPlayer(player1)
    assertThrows[AssertionError] {
      newGame.addPlayer(player1)
    }


    assert(newGame.gameIdentity == game.gameIdentity)
    assert(newGame.numPlayers == game.numPlayers + 1)
  }

  test("Given a burraco game with 3 players, when I add the fourth player, then I have a game with 4 players") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val actualGame = game.addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assert(actualGame.numPlayers == 4)
  }

  test("Given a burraco game with 4 players, when I add the fifth player, then I receive an error") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assertThrows[AssertionError]{
      game.addPlayer(PlayerNotAssigned(PlayerIdentity()))
    }
  }

  test("Given a burraco game with 3 players, when I check if a player is in the game, then I receive a positive feedback") {
    val player1 = PlayerNotAssigned(PlayerIdentity())
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer().addPlayer(player1)

    val expectedResult = true

    assert(game.isAlreadyAPlayer(player1.playerIdentity) == expectedResult)
  }

  test("Given a burraco game with 2 players, when I check if a new player is in the game, then I receive a negative feedback") {
    val player1 = PlayerNotAssigned(PlayerIdentity())
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()

    val expectedResult = false

    assert(game.isAlreadyAPlayer(player1.playerIdentity) == expectedResult)
  }

  test("Given a burraco game with 2 players, when I start the game, then I started the burraco game") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()
    val actualGame = game.start()
    assert(actualGame.isInstanceOf[BurracoGameInitiatedTurnStart])
  }

  test("Given a burraco game with 1 players, when I start the game, then I receive an error") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer(true)
    assertThrows[AssertionError]{
      game.start()
    }
  }

  test("initialise a game with 2 players") {
    val totalCardsInGameExpected = 108

    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt: BurracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    val gameInitiated = game.start()

    assert(gameInitiated.numPlayers == game.numPlayers)
    assert(gameInitiated.listOfPlayers.exists(playerInGame => game.listOfPlayers().exists(playerWaiting => playerWaiting.playerIdentity == playerInGame.playerIdentity)))
    assert(gameInitiated.identity() == game.gameIdentity)

  }


}
