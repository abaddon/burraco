package com.abaddon83.cardsGames.burracoGames.services

import com.abaddon83.cardsGames.burracoGames.commands.{AddPlayerCmd, CreateNewBurracoGameCmd, OrganisePlayerCardsCmd, PickUpACardFromDeckCmd, PickUpCardsFromDiscardPileCmd, StartGameCmd}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.{GameRepositoryPort, PlayerPort}
import com.abaddon83.cardsGames.burracoGames.queries.{FindBurracoGameInitiatedQuery, FindBurracoGameInitiatedTurnExecQuery, FindBurracoGameWaitingQuery}
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.CommandDispatcher
import com.abaddon83.libs.cqs.queries.QueryDispatcher

import scala.concurrent.Future


class BurracoGameService(
                          burracoGameRepositoryPort: GameRepositoryPort,
                          playerPort: PlayerPort,
                          commandDispatcher: CommandDispatcher,
                          queryDispatcher: QueryDispatcher
                        )(implicit val ec: scala.concurrent.ExecutionContext /* = scala.concurrent.ExecutionContext.global*/) {

  def createNewBurracoGame(): Future[BurracoGameWaitingPlayers] = {
    val gameIdentity = GameIdentity()
    val command = CreateNewBurracoGameCmd(gameIdentity)
    val query = FindBurracoGameWaitingQuery(gameIdentity)
    commandDispatcher.dispatch[CreateNewBurracoGameCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers](query)

  }

  def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    val command = AddPlayerCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameWaitingQuery(gameIdentity)
    commandDispatcher.dispatch[AddPlayerCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers](query)
  }

  def startGame(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart] = {
    val command = StartGameCmd(gameIdentity)
    val query = FindBurracoGameInitiatedTurnExecQuery(gameIdentity)

    commandDispatcher.dispatch[StartGameCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameInitiatedTurnExecQuery, BurracoGameInitiatedTurnStart](query)
  }

  def pickUpACardFromDeck(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameInitiatedTurnExecution] = {
    val command = PickUpACardFromDeckCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameInitiatedTurnExecQuery(gameIdentity)

    commandDispatcher.dispatch[PickUpACardFromDeckCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameInitiatedTurnExecQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def pickUpCardsFromDiscardPile(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): Future[BurracoGameInitiatedTurnExecution] = {
    val command = PickUpCardsFromDiscardPileCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameInitiatedTurnExecQuery(gameIdentity)

    commandDispatcher.dispatch[PickUpCardsFromDiscardPileCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameInitiatedTurnExecQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def organisePlayerCard(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, orderedCards: List[Card]): Future[BurracoGameInitiated] = {
    val command = OrganisePlayerCardsCmd(gameIdentity, playerIdentity,orderedCards)
    val query = FindBurracoGameInitiatedQuery(gameIdentity)

    commandDispatcher.dispatch[OrganisePlayerCardsCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameInitiatedQuery, BurracoGameInitiated](query)
  }


}
