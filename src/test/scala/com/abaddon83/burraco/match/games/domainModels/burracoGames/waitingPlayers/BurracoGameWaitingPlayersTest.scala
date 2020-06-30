package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.{BurracoCardsDealt, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.`match`.games.domainModels.{BurracoPlayer, PlayerNotAssigned, burracoGames}
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.mocks.BurracoGameInitTurnTestFactory
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameWaitingPlayersTest extends AnyFunSuite {


  test("Given a burraco game, when I add a new player, then the num of player increase") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()

    val newGame = game.addPlayer(BurracoPlayer(PlayerIdentity()))

    assert(newGame.gameIdentity == game.gameIdentity)
    assert(newGame.numPlayers == game.numPlayers + 1)
  }

  test("Given a burraco game, when I add a player 2 times, then I receive an error") {
    val game = BurracoGameInitTurnTestFactory().buildWaitingPlayer()
    val player1 = BurracoPlayer(PlayerIdentity())
    val newGame = game.addPlayer(player1)
    assertThrows[AssertionError] {
      newGame.addPlayer(player1)
    }


    assert(newGame.gameIdentity == game.gameIdentity)
    assert(newGame.numPlayers == game.numPlayers + 1)
  }

  test("add 4 players") {
    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(BurracoPlayer(PlayerIdentity()))
      .addPlayer(BurracoPlayer(PlayerIdentity()))
      .addPlayer(BurracoPlayer(PlayerIdentity()))
      .addPlayer(BurracoPlayer(PlayerIdentity()))

    assert(game.gameIdentity == game.gameIdentity)
    assert(game.numPlayers == 4)
  }

  test("add 5 players should fail") {

    assertThrows[AssertionError] {
      burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
    }
  }

  test("add 2 times the same player should fail") {
    val player = PlayerNotAssigned(PlayerIdentity())
    assertThrows[AssertionError] {
      burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
        .addPlayer(player)
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(player)
    }
  }

  test("initialise a game with 2 players") {
    val totalCardsInGameExpected = 108

    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt: BurracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    val gameInitiated = game.initiate(burracoCardsDealt)

    assert(gameInitiated.numPlayers == game.numPlayers)
    assert(gameInitiated.listOfPlayers.exists(playerInGame => game.listOfPlayers().exists(playerWaiting => playerWaiting.playerIdentity == playerInGame.playerIdentity)))
    assert(gameInitiated.identity() == game.gameIdentity)

  }


}
