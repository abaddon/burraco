package com.abaddon83.burraco.`match`.games.commands.burracoGames

import com.abaddon83.burraco.`match`.games.domainModels.PlayerNotAssigned
import com.abaddon83.burraco.mocks.{GameFactory, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class InitialiseGameTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = InitialiseGameHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)
  //val player1 = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")
  //val player2 = PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")


  test("async, initialise the game with no player, should fail") {
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

    val command = InitialiseGame(gameIdentity = gameIdentity)
    handler.handleAsync(command) map { result =>
      assertThrows[UnsupportedOperationException]{
        result
      }
    }
  }

  test("async, initialise the game with a player, should fail") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player1))
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
      .addPlayer(player1)

    val command = InitialiseGame(gameIdentity = gameIdentity)

      handler.handleAsync(command) map {result =>
        assertThrows[AssertionError]{
          result
        }
      }
  }

  test("async, initialise the game with two players") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player1))
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player2))

    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
      .addPlayer(player1)
      .addPlayer(player2)

    val command = InitialiseGame(gameIdentity = gameIdentity)

    handler.handleAsync(command) foreach { r =>
      mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnStartBy(gameIdentity) map { result =>
        assert(result.identity() == gameIdentity)
      }
    }

  }

  test("sync, initialise the game with no player, should fail") {
    val gameIdentity = GameIdentity()
    GameFactory.build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

    val command = InitialiseGame(gameIdentity = gameIdentity)

    assertThrows[UnsupportedOperationException]{
      handler.handle(command)
    }
  }

  test("sync, initialise the game with a player, should fail") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player1))

    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
      .addPlayer(player1)

    val command = InitialiseGame(gameIdentity = gameIdentity)

    assertThrows[AssertionError]{
      handler.handle(command)
    }
  }

  test("sync, initialise the game with two players") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player1))
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player2))

    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
      .addPlayer(player1)
      .addPlayer(player2)

    val command = InitialiseGame(gameIdentity = gameIdentity)

    handler.handle(command)
    mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnStartBy(gameIdentity) map { result =>
      assert(result.identity() == gameIdentity)
    }
  }
}
