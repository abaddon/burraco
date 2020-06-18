package com.abaddon83.burraco.`match`.games.commands.games

import com.abaddon83.burraco.mocks.{MockBurracoGameRepositoryAdapter, MockExecutionContext}
import com.abaddon83.burraco.shares.games.{GameIdentity, GameTypes}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class CreateNewGameTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext {

  val handler = CreateNewGameHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)
  val globalGameIdentity = GameIdentity()

  test("async, create new game") {
    val gameIdentity = globalGameIdentity
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    assert(handler.handleAsync(command).futureValue.isInstanceOf[Unit])

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.gameIdentity == gameIdentity)
  }

  test("sync, create new game") {
    val gameIdentity = GameIdentity()
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    assert(handler.handle(command).isInstanceOf[Unit])

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.gameIdentity == gameIdentity)
  }

  test("async, create a new game with the same gameIdentity") {
    val gameIdentity = globalGameIdentity
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    assertThrows[AssertionError]{
      handler.handleAsync(command)
    }
  }

  test("sync, create a new game with the same gameIdentity") {
    val gameIdentity = globalGameIdentity
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    assertThrows[AssertionError]{
      handler.handle(command)
    }
  }

}
