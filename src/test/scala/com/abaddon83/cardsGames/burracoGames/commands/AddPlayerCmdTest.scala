package com.abaddon83.cardsGames.burracoGames.commands

import com.abaddon83.cardsGames.burracoGames.domainModels.PlayerNotAssigned
import com.abaddon83.cardsGames.mocks.{GameFactoryMock, MockBurracoGameRepositoryAdapter, MockPlayerAdapter}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class AddPlayerCmdTest extends AnyFunSuite
  with ScalaFutures
  with BeforeAndAfter
  with MockBurracoGameRepositoryAdapter
  with MockPlayerAdapter{

  val handler = new AddPlayerHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter )

  test("async, add new player") {
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

    GameFactoryMock
      .build(gameIdentity).persist()

    val command = AddPlayerCmd(gameIdentity = gameIdentity,playerToAdd = PlayerNotAssigned(playerIdentity))
    handler.handleAsync(command).foreach{ r =>
      val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
      assert(game.isAlreadyAPlayer(playerIdentity))
    }

  }

  test("async, add 2 times a player, should fail") {
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

    GameFactoryMock
      .build(gameIdentity).persist()
    val command = AddPlayerCmd(gameIdentity = gameIdentity,playerToAdd = PlayerNotAssigned(playerIdentity))
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

    GameFactoryMock
      .build(gameIdentity).persist()
    val command = AddPlayerCmd(gameIdentity = gameIdentity,playerToAdd = PlayerNotAssigned(playerIdentity))
    handler.handle(command)

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.isAlreadyAPlayer(playerIdentity))
  }

  test("sync, add 2 times a player, should fail") {
    val playerIdentity = PlayerIdentity()
    val gameIdentity = GameIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(playerIdentity))

    GameFactoryMock
      .build(gameIdentity).persist()

    val command = AddPlayerCmd(gameIdentity = gameIdentity,playerToAdd = PlayerNotAssigned(playerIdentity))
    handler.handle(command)

    assertThrows[AssertionError] {
      handler.handle(command)
    }
  }

}
