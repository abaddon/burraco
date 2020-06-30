package com.abaddon83.cardsGames.mocks

import com.abaddon83.cardsGames.burracoGames.domainModels.PlayerNotAssigned
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.services.BurracoDealerFactory
import com.abaddon83.cardsGames.shares.games.{GameIdentity, GameTypes}
import com.abaddon83.cardsGames.shares.players.PlayerIdentity



case class GameFactoryMock(
                            game: BurracoGame)
                          (implicit val ec: scala.concurrent.ExecutionContext)
  extends MockBurracoGameRepositoryAdapter with MockPlayerAdapter {

  def addPlayerNotAssigned(playerIdentity: PlayerIdentity): GameFactoryMock = {
    val player = PlayerNotAssigned(playerIdentity)
    mockPlayerAdapter.mockPlayer().addOne(player)
    this.copy(
      game = game.asInstanceOf[BurracoGameWaitingPlayers].addPlayer(player)
    )
  }

  def initialise(): GameFactoryMock = {
    val gameWaiting = game.asInstanceOf[BurracoGameWaitingPlayers]
    val burracoCardsDealt = BurracoDealerFactory(gameWaiting).dealBurracoCards()
    this.copy(
      game = gameWaiting.start()
    )
  }

  def pickUpACardFromDeck(playerIdentity: PlayerIdentity): GameFactoryMock = {
    val gameStarted = game.asInstanceOf[BurracoGameInitiatedTurnStart]
    this.copy(
      game = gameStarted.pickUpACardFromDeck(playerIdentity = playerIdentity)
    )
  }
  def dropCardOnDiscardPile(playerIdentity: PlayerIdentity): GameFactoryMock = {
    val gameExecution = game.asInstanceOf[BurracoGameInitiatedTurnExecution]
    this.copy(
      game = gameExecution.dropCardOnDiscardPile(playerIdentity = playerIdentity ,card = gameExecution.playerCards(playerIdentity).head)
    )
  }


  def persist() = {
    game match {
      //case game: BurracoGameCompleted => mockBurracoGameRepositoryAdapter.save(game)
      case game: BurracoGameInitiatedTurnStart => mockBurracoGameRepositoryAdapter.save(game)
      case game: BurracoGameInitiatedTurnExecution => mockBurracoGameRepositoryAdapter.save(game)
      case game: BurracoGameInitiatedTurnEnd => mockBurracoGameRepositoryAdapter.save(game)
      case game: BurracoGameWaitingPlayers => mockBurracoGameRepositoryAdapter.save(game)
      case _ => throw new Exception("GameFactory mockUp failed to persist the game")
    }
  }
}

object GameFactoryMock{
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def build(gameIdentity: GameIdentity): GameFactoryMock = {

    val game = BurracoGame.createNewBurracoGame(gameIdentity)

    new GameFactoryMock(game)
  }

}
