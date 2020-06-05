package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameTest extends AnyFunSuite{

  test("create a Burraco match") {
    val game = BurracoGame.createNewBurracoGame()
    assert(game.isInstanceOf[BurracoGameWaitingPlayers])
    assert(game.numPlayers == 0)
  }

  test("add a player"){
    val game = BurracoGame.createNewBurracoGame()

    val newGame = game.addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assert(newGame.gameIdentity == game.gameIdentity)
    assert(newGame.numPlayers == game.numPlayers+1)
  }

  test("add 4 players"){
    val game = BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assert(game.gameIdentity == game.gameIdentity)
    assert(game.numPlayers == 4)
  }

  test("add 5 players should fail"){

    assertThrows[AssertionError]{
      BurracoGame.createNewBurracoGame()
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
    }
  }

  test("add 2 times the same player should fail"){
    val player = PlayerNotAssigned(PlayerIdentity())
    assertThrows[AssertionError]{
      BurracoGame.createNewBurracoGame()
        .addPlayer(player)
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(player)
    }
  }

  test("initialise a game with 2 players"){
    val totalCardsInGameExpected= 108

    val game = BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt: BurracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    val gameInitiated = game.initiate(burracoCardsDealt)

    assert(gameInitiated.numPlayers == game.numPlayers)
    assert(gameInitiated.listOfPlayers.exists( playerInGame => game.listOfPlayers().exists(playerWaiting => playerWaiting.playerIdentity == playerInGame.playerIdentity)))
    assert(gameInitiated.totalCardsInGame == totalCardsInGameExpected)
    assert(gameInitiated.gameIdentity == game.gameIdentity)

  }


}
