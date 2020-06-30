package com.abaddon83.cardsGames.burracoGames.commands.games

import com.abaddon83.cardsGames.mocks.{MockBurracoGameRepositoryAdapter, MockExecutionContext}
import com.abaddon83.cardsGames.shares.games.{GameIdentity, GameTypes}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class CreateNewGameTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext {

  val handler = new CreateNewGameHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)


  test("async, create new game") {
    val gameIdentity = GameIdentity()
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    //assert(handler.handleAsync(command).futureValue.isInstanceOf[Unit])
    handler.handleAsync(command).foreach{ r =>
      val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
      assert(game.gameIdentity == gameIdentity)
    }

  }

  test("sync, create new game") {
    val gameIdentity = GameIdentity()
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    handler.handle(command)

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.gameIdentity == gameIdentity)
  }

  test("async, create a new game with the same gameIdentity") {
    val gameIdentity = GameIdentity()
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    handler.handle(command)
    assertThrows[AssertionError]{
      handler.handleAsync(command)
    }
  }

  test("sync, create a new game with the same gameIdentity") {
    val gameIdentity = GameIdentity()
    val burracoGameType = GameTypes.Burraco
    val command = CreateNewGame(gameIdentity = gameIdentity,burracoGameType)
    handler.handle(command)
    assertThrows[AssertionError]{
      handler.handle(command)
    }
  }

}
