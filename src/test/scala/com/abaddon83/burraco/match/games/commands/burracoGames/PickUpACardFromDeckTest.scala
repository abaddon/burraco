package com.abaddon83.burraco.`match`.games.commands.burracoGames

import com.abaddon83.burraco.mocks.{GameFactory, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite

class PickUpACardFromDeckTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = PickUpACardFromDeckHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)
  val player1 = PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")
  val player2 = PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")


  test("async, during the player turn, pick up a card from the deck") {
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
      .addPlayer(player1)
      .addPlayer(player2)
      .initialise()

    val command = PickUpACardFromDeck(gameIdentity = gameIdentity, playerIdentity = player1)
    handler.handleAsync(command).foreach{r =>
      val gameTurnExecution = mockBurracoGameRepositoryAdapter.findBurracoGameInitiatedTurnExecutionBy(gameIdentity).futureValue
      assert(gameTurnExecution.identity() == gameIdentity)
    }

  }

  test("async, not during the player turn, pick up a card from the deck") {
    val gameIdentity = GameIdentity()
    GameFactory
      .build(gameIdentity,mockBurracoGameRepositoryAdapter,mockPlayerAdapter)
      .addPlayer(player1)
      .addPlayer(player2)
      .initialise()

    val command = PickUpACardFromDeck(gameIdentity = gameIdentity, playerIdentity = player2)
    handler.handleAsync(command).foreach { result =>
      assertThrows[UnsupportedOperationException]{
        result
      }
    }
  }
}
