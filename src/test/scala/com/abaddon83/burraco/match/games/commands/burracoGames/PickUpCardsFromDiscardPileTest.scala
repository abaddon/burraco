package com.abaddon83.burraco.`match`.games.commands.burracoGames

import com.abaddon83.burraco.`match`.games.domainModels.PlayerNotAssigned
import com.abaddon83.burraco.mocks.{GameFactoryMock, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class PickUpCardsFromDiscardPileTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = PickUpCardsFromDiscardPileHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)

  test("async, during the player turn, pick up the discard pile") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()


    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .persist()

    val command = PickUpCardsFromDiscardPile(gameIdentity = gameIdentity, playerIdentity = player1)
    handler.handleAsync(command).foreach{r =>
      val gameTurnExecution = mockBurracoGameRepositoryAdapter.findBurracoGameInitiatedTurnExecutionBy(gameIdentity).futureValue
      assert(gameTurnExecution.identity() == gameIdentity)
    }
  }

  test("async, not during the player turn, pick up the discard pile") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player1))
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player2))

    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .persist()

    val command = PickUpCardsFromDiscardPile(gameIdentity = gameIdentity, playerIdentity = player2)
    handler.handleAsync(command).foreach { result =>
      assertThrows[UnsupportedOperationException]{
        result
      }
    }
  }

  test("sync, during the player turn, pick up the discard pile") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()

    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .persist()

    val command = PickUpCardsFromDiscardPile(gameIdentity = gameIdentity, playerIdentity = player1)
    handler.handle(command)
    val gameTurnExecution = mockBurracoGameRepositoryAdapter.findBurracoGameInitiatedTurnExecutionBy(gameIdentity).futureValue
    assert(gameTurnExecution.identity() == gameIdentity)

  }

  test("sync, not during the player turn, pick up the discard pile") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player1))
    mockPlayerAdapter.mockPlayer().addOne(PlayerNotAssigned(player2))

    GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .persist()

    val command = PickUpCardsFromDiscardPile(gameIdentity = gameIdentity, playerIdentity = player2)

    assertThrows[UnsupportedOperationException]{
      handler.handle(command)
    }

  }

}
