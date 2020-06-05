package com.abaddon83.burraco.`match`.games.services

import java.util.NoSuchElementException

import com.abaddon83.burraco.mocks.{MockBurracoGameRepositoryAdapter, MockPlayerAdapter}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite


class GameServiceTest extends AnyFunSuite
    with ScalaFutures
    with MockBurracoGameRepositoryAdapter
    with MockPlayerAdapter {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  val gameService = new GameService(mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

  test("create a new Burraco game with a valid user"){

    val playerIdentity = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")

    val burracoGameWaitingPlayers = gameService.createNewBurracoGame(playerIdentity).futureValue
    assert(burracoGameWaitingPlayers.listOfPlayers.size == 1)
  }

  test("create a new Burraco game with a not existing user, should fail"){
    val playerIdentity = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532fe")
    assert(gameService.createNewBurracoGame(playerIdentity).failed.futureValue.isInstanceOf[NoSuchElementException])
  }

  test("create a new Burraco game with a existing user but already assigned to another game, should fail"){
    val playerIdentity = PlayerIdentity("a11de97d-d46e-4d73-9f74-a9b0bf7665a5")
    assert(gameService.createNewBurracoGame(playerIdentity).failed.futureValue.isInstanceOf[NoSuchElementException])
  }
}


