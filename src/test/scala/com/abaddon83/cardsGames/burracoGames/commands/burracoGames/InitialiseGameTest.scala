package com.abaddon83.cardsGames.burracoGames.commands.burracoGames

import com.abaddon83.cardsGames.burracoGames.commands.{StartGameCmd, StartGameHandler}
import com.abaddon83.cardsGames.mocks.{GameFactoryMock, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class InitialiseGameTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = new StartGameHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)
  //val player1 = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")
  //val player2 = PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")


  test("async, initialise the game with no player, should fail") {
    val gameIdentity = GameIdentity()
    GameFactoryMock
      .build(gameIdentity).persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)
    handler.handleAsync(command) map { result =>
      assertThrows[UnsupportedOperationException]{
        result
      }
    }
  }

  test("async, initialise the game with a player, should fail") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)

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

    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)

    handler.handleAsync(command) foreach { r =>
      mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnStartBy(gameIdentity) map { result =>
        assert(result.identity() == gameIdentity)
      }
    }

  }

  test("sync, initialise the game with no player, should fail") {
    val gameIdentity = GameIdentity()
    GameFactoryMock.build(gameIdentity).persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)

    assertThrows[AssertionError]{
      handler.handle(command)
    }
  }

  test("sync, initialise the game with a player, should fail") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()


    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)

    assertThrows[AssertionError]{
      handler.handle(command)
    }
  }

  test("sync, initialise the game with two players") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()

    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)

    handler.handle(command)
    mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnStartBy(gameIdentity) map { result =>
      assert(result.identity() == gameIdentity)
    }
  }
}
