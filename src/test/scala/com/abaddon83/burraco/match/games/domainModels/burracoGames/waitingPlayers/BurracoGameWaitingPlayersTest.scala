package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.{BurracoCardsDealt, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.`match`.games.domainModels.{PlayerNotAssigned, burracoGames}
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameWaitingPlayersTest extends AnyFunSuite{

  test("create a Burraco match") {
    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
    assert(game.isInstanceOf[BurracoGameWaitingPlayers])
    assert(game.numPlayers == 0)
  }

  test("add a player"){
    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())

    val newGame = game.addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assert(newGame.gameIdentity == game.gameIdentity)
    assert(newGame.numPlayers == game.numPlayers+1)
  }

  test("add 4 players"){
    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    assert(game.gameIdentity == game.gameIdentity)
    assert(game.numPlayers == 4)
  }

  test("add 5 players should fail"){

    assertThrows[AssertionError]{
      burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
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
      burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
        .addPlayer(player)
        .addPlayer(PlayerNotAssigned(PlayerIdentity()))
        .addPlayer(player)
    }
  }

  test("initialise a game with 2 players"){
    val totalCardsInGameExpected= 108

    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt: BurracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    val gameInitiated = game.initiate(burracoCardsDealt)

    assert(gameInitiated.numPlayers == game.numPlayers)
    assert(gameInitiated.listOfPlayers.exists( playerInGame => game.listOfPlayers().exists(playerWaiting => playerWaiting.playerIdentity == playerInGame.playerIdentity)))
    assert(gameInitiated.identity() == game.gameIdentity)

  }


}
