package com.abaddon83.burraco.`match`.games.commands.burracoGames

import com.abaddon83.burraco.`match`.games.commands.burracoGames.{AddPlayer, AddPlayerHandler}
import com.abaddon83.burraco.`match`.games.commands.games.{CreateNewGame, CreateNewGameHandler}
import com.abaddon83.burraco.mocks.{GameFactory, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.burraco.shares.games.{GameIdentity, GameTypes}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

import scala.util.{Failure, Success}

class AddPlayerTest extends AnyFunSuite
  with ScalaFutures
  with BeforeAndAfter
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = AddPlayerHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter,playerPort = mockPlayerAdapter )
  val player1 = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")
  val player2 = PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")


  test("async, add new player") {
    val playerIdentity = player1
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    assert(handler.handleAsync(command).futureValue.isInstanceOf[Unit])



    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.isAlreadyAPlayer(playerIdentity))
  }

  test("async, add 2 times a player, should fail") {
    val playerIdentity = player1
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handleAsync(command).foreach { r =>
      mockPlayerAdapter.updatePlayerStatusToPlayerAssigned(playerIdentity)
      handler.handleAsync(command).foreach{ r1 =>
        assertThrows[NoSuchElementException]{
          r1
        }
      }
    }
  }

  test("sync, add new player") {
    val playerIdentity = player2
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handle(command)

    mockPlayerAdapter.updatePlayerStatusToPlayerAssigned(playerIdentity)

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.isAlreadyAPlayer(playerIdentity))
  }

  test("sync, add 2 times a player, should fail") {
    val playerIdentity = player2
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handle(command)
    mockPlayerAdapter.updatePlayerStatusToPlayerAssigned(playerIdentity)

    assertThrows[NoSuchElementException] {
      handler.handle(command)
    }
  }

}
