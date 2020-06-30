package com.abaddon83.cardsGames.burracoGames.commands.burracoGames

import com.abaddon83.cardsGames.burracoGames.commands.{PickUpCardsFromDiscardPileCmd, PickUpCardsFromDiscardPileHandler}
import com.abaddon83.cardsGames.burracoGames.domainModels.PlayerNotAssigned
import com.abaddon83.cardsGames.mocks.{GameFactoryMock, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class PickUpCardsFromDiscardPileTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = new PickUpCardsFromDiscardPileHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)

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

    val command = PickUpCardsFromDiscardPileCmd(gameIdentity = gameIdentity, playerIdentity = player1)
    handler.handleAsync(command).foreach{r =>
      val gameTurnExecution = mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnExecutionBy(gameIdentity).futureValue
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

    val command = PickUpCardsFromDiscardPileCmd(gameIdentity = gameIdentity, playerIdentity = player2)
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

    val command = PickUpCardsFromDiscardPileCmd(gameIdentity = gameIdentity, playerIdentity = player1)
    handler.handle(command)
    val gameTurnExecution = mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnExecutionBy(gameIdentity).futureValue
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

    val command = PickUpCardsFromDiscardPileCmd(gameIdentity = gameIdentity, playerIdentity = player2)

    assertThrows[UnsupportedOperationException]{
      handler.handle(command)
    }

  }

}
