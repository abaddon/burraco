package com.abaddon83.burraco.`match`.games.commands.burracoGames

import com.abaddon83.burraco.`match`.games.commands.burracoGames.{AddPlayer, AddPlayerHandler}
import com.abaddon83.burraco.`match`.games.domainModels.PlayerNotAssigned
import com.abaddon83.burraco.mocks.{GameFactory, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.burraco.shares.games.{GameIdentity}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class AddPlayerTest extends AnyFunSuite
  with ScalaFutures
  with BeforeAndAfter
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = AddPlayerHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter,playerPort = mockPlayerAdapter )



  test("async, add new player") {
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handleAsync(command).foreach{ r =>
      val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
      assert(game.isAlreadyAPlayer(playerIdentity))
    }

  }

  test("async, add 2 times a player, should fail") {
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

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
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handle(command)

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.isAlreadyAPlayer(playerIdentity))
  }

  test("sync, add 2 times a player, should fail") {
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)

    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handle(command)

    assertThrows[AssertionError] {
      handler.handle(command)
    }
  }

}
