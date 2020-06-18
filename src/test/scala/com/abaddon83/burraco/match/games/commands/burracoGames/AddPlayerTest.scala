package com.abaddon83.burraco.`match`.games.commands.burracoGames

import com.abaddon83.burraco.`match`.games.commands.burracoGames.{AddPlayer, AddPlayerHandler}
import com.abaddon83.burraco.`match`.games.commands.games.{CreateNewGame, CreateNewGameHandler}
import com.abaddon83.burraco.mocks.{MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
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
  val gameIdentity = GameIdentity()
  val player1 = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")
  val player2 = PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")

  before {
    if(!mockBurracoGameRepositoryAdapter.exists(gameIdentity)){
      val command = CreateNewGame(gameIdentity = gameIdentity,GameTypes.Burraco)
      CreateNewGameHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter).handle(command)
    }
  }

  test("async, add new player") {
    val playerIdentity = player1
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    assert(handler.handleAsync(command).futureValue.isInstanceOf[Unit])

    mockPlayerAdapter.updatePlayerStatusToPlayerAssigned(playerIdentity)

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.isAlreadyAPlayer(playerIdentity))
  }

  test("async, add 2 times a player, should fail") {
    val playerIdentity = player1
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)

    handler.handleAsync(command).onComplete {
      case Failure(exception) => assert(exception.isInstanceOf[NoSuchElementException])
      case Success(_) => assert(false)
    }
  }

  test("sync, add new player") {
    val playerIdentity = player2
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    handler.handle(command)

    mockPlayerAdapter.updatePlayerStatusToPlayerAssigned(playerIdentity)

    val game = mockBurracoGameRepositoryAdapter.findBurracoGameWaitingPlayersBy(gameIdentity).futureValue
    assert(game.isAlreadyAPlayer(playerIdentity))
  }

  test("sync, add 2 times a player, should fail") {
    val playerIdentity = player2
    val command = AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)

    assertThrows[NoSuchElementException] {
      handler.handle(command)
    }
  }

}
