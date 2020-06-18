package com.abaddon83.burraco.`match`.games.services

import java.util.NoSuchElementException

import com.abaddon83.burraco.mocks.{MockBurracoGameRepositoryAdapter, MockPlayerAdapter}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite


class BurracoGameServiceTest extends AnyFunSuite
    with ScalaFutures
    with MockBurracoGameRepositoryAdapter
    with MockPlayerAdapter {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  val gameService = new BurracoGameService(mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

  /*test("create a new Burraco game with a valid user"){

    val playerIdentity = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")

    val burracoGameWaitingPlayers = gameService.createNewBurracoGame(playerIdentity).futureValue
    assert(burracoGameWaitingPlayers.listOfPlayers.size == 0)
  }*/

  test("create a new Burraco game with a not existing user, should fail"){
    val playerIdentity = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532fe")
    assert(gameService.createNewBurracoGame(playerIdentity).failed.futureValue.isInstanceOf[NoSuchElementException])
  }

  test("create a new Burraco game with a existing user but already assigned to another game, should fail"){
    val playerIdentity = PlayerIdentity("a11de97d-d46e-4d73-9f74-a9b0bf7665a5")
    assert(gameService.createNewBurracoGame(playerIdentity).failed.futureValue.isInstanceOf[NoSuchElementException])
  }

 /*
 test("list of all BurracoGamesWaitingPlayers") {
    val list = gameService.allBurracoGamesWaitingPlayers().futureValue
    assert(list.size == 1)
  }

  test("add a player to the game") {
    val list = gameService.allBurracoGamesWaitingPlayers().futureValue
    val gameIdentity = list(0).gameIdentity
    val playerIdentity = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")
    val gameResult = gameService.addPlayer(gameIdentity,playerIdentity).futureValue

    assert(gameResult.numPlayers == 1)
  }

  test("add the same player to the game") {
    val list = gameService.allBurracoGamesWaitingPlayers().futureValue
    val gameIdentity = list(0).gameIdentity
    val playerIdentity = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")

    assertThrows[Exception]{
      gameService.addPlayer(gameIdentity,playerIdentity).futureValue
    }
  }

  test("initialise the game with only one player, should fail"){
    val list = gameService.allBurracoGamesWaitingPlayers().futureValue
    val gameIdentity = list(0).gameIdentity
    assertThrows[Exception] {
      gameService.initialiseGame(gameIdentity).futureValue
    }
  }

  test("initialise the game with 2 player"){
    val list = gameService.allBurracoGamesWaitingPlayers().futureValue
    val gameIdentity = list(0).gameIdentity
    val playerIdentity = PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff731")
    gameService.addPlayer(gameIdentity,playerIdentity).futureValue
    val gameInit = gameService.initialiseGame(gameIdentity).futureValue

    assert(gameInit.gameIdentity == gameIdentity)
    assert(gameService.allBurracoGamesWaitingPlayers().futureValue.size == 0)
  }*/
}


