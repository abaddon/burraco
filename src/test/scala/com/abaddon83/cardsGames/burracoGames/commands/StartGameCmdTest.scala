package com.abaddon83.cardsGames.burracoGames.commands

import com.abaddon83.cardsGames.mocks.{GameFactoryMock, MockBurracoGameRepositoryAdapter, MockPlayerAdapter}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.cardsGames.testutils.WithExecutionContext
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class StartGameCmdTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with WithExecutionContext
  with MockPlayerAdapter{

  val handler = new StartGameHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)


  test("Given the game doesn't have players,when I execute async the command, then the command fails") {
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

  test("Given the game has a players,,when I execute async the command, then the command fails") {
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

  test("Given the game has 2 players,,when I execute async the command, then the command is executed correctly") {
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

  test("Given the game doesn't have players,when I execute sync the command, then the command fails") {
    val gameIdentity = GameIdentity()
    GameFactoryMock.build(gameIdentity).persist()

    val command = StartGameCmd(gameIdentity = gameIdentity)

    assertThrows[AssertionError]{
      handler.handle(command)
    }
  }

  test("Given the game has 1 player,when I execute async the command, then the command fails") {
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

  test("Given the game has 2 players,,when I execute sync the command, then the command is executed correctly") {
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
