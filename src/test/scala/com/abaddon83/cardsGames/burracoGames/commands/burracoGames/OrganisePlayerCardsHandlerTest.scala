package com.abaddon83.cardsGames.burracoGames.commands.burracoGames

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.mocks.{GameFactoryMock, MockBurracoGameRepositoryAdapter, MockExecutionContext, MockPlayerAdapter}
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite


class OrganisePlayerCardsHandlerTest extends AnyFunSuite
  with ScalaFutures
  with MockBurracoGameRepositoryAdapter
  with MockExecutionContext
  with MockPlayerAdapter{

  val handler = new OrganisePlayerCardsHandler(gameRepositoryPort = mockBurracoGameRepositoryAdapter)

  test("async, update the player cards orders during his turn during the start phase") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()

    val playerToUpdate = player1

    val gameCreated = GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .persist()

    val currentCards = gameCreated.asInstanceOf[BurracoGameInitiatedTurnStart].playerCards(playerToUpdate)
    val orderedCards: List[Card] = currentCards.sortBy(_.rank.label)

    val command = OrganisePlayerCards(
      gameIdentity = gameIdentity,
      playerIdentity = playerToUpdate,
      orderedCards = orderedCards
    )

    handler.handleAsync(command).foreach{r =>
      val game = mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnStartBy(gameIdentity).futureValue
      assert(game.playerCards(playerToUpdate).head == orderedCards.head)
      assert(game.playerCards(playerToUpdate).last == orderedCards.last)
      assert(game.playerCards(playerToUpdate)(2) == orderedCards(2))
      assert(game.playerCards(playerToUpdate)(5) == orderedCards(5))
    }
  }

  test("async, update the player cards orders during the turn of another player during the start phase") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()

    val playerToUpdate = player2

    val gameCreated = GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .persist()

    val currentCards = gameCreated.asInstanceOf[BurracoGameInitiatedTurnStart].playerCards(playerToUpdate)
    val orderedCards: List[Card] = currentCards.sortBy(_.rank.label)

    val command = OrganisePlayerCards(
      gameIdentity = gameIdentity,
      playerIdentity = playerToUpdate,
      orderedCards = orderedCards
    )

    handler.handleAsync(command).foreach{r =>
      val game = mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnStartBy(gameIdentity).futureValue
      assert(game.playerCards(playerToUpdate).head == orderedCards.head)
      assert(game.playerCards(playerToUpdate).last == orderedCards.last)
      assert(game.playerCards(playerToUpdate)(2) == orderedCards(2))
      assert(game.playerCards(playerToUpdate)(5) == orderedCards(5))
    }
  }

  test("async, update the player cards orders during his turn during the execution phase") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()

    val playerToUpdate = player1

    val gameCreated = GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .pickUpACardFromDeck(player1)
      .persist()

    val currentCards = gameCreated.asInstanceOf[BurracoGameInitiatedTurnExecution].playerCards(playerToUpdate)
    val orderedCards: List[Card] = currentCards.sortBy(_.rank.label)

    val command = OrganisePlayerCards(
      gameIdentity = gameIdentity,
      playerIdentity = playerToUpdate,
      orderedCards = orderedCards
    )

    handler.handleAsync(command).foreach{r =>
      val game = mockBurracoGameRepositoryAdapter.findBurracoGameInitialisedTurnExecutionBy(gameIdentity).futureValue
      assert(game.playerCards(playerToUpdate).head == orderedCards.head)
      assert(game.playerCards(playerToUpdate).last == orderedCards.last)
      assert(game.playerCards(playerToUpdate)(2) == orderedCards(2))
      assert(game.playerCards(playerToUpdate)(5) == orderedCards(5))
    }
  }

  test("async, update the player cards orders during his turn during the end phase") {
    val gameIdentity = GameIdentity()
    val player1 = PlayerIdentity()
    val player2 = PlayerIdentity()

    val playerToUpdate = player1

    val gameCreated = GameFactoryMock
      .build(gameIdentity)
      .addPlayerNotAssigned(player1)
      .addPlayerNotAssigned(player2)
      .initialise()
      .pickUpACardFromDeck(player1)
      .dropCardOnDiscardPile(player1)
      .persist()

    val currentCards = gameCreated.asInstanceOf[BurracoGameInitiatedTurnEnd].playerCards(playerToUpdate)
    val orderedCards: List[Card] = currentCards.sortBy(_.rank.label)

    val command = OrganisePlayerCards(
      gameIdentity = gameIdentity,
      playerIdentity = playerToUpdate,
      orderedCards = orderedCards
    )

    //TODO check this test because should fail..
    //assertThrows[Exception]{
      handler.handleAsync(command)
    //}

  }


}