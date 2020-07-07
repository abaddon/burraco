package com.abaddon83.cardsGames.burracoGames.services

import com.abaddon83.cardsGames.burracoGames.commands.{AddPlayerCmd, AppendCardOnBurracoCmd, CreateNewBurracoGameCmd, DropCardOnDiscardPileCmd, DropScaleCmd, DropTrisCmd, EndGameCmd, EndPlayerTurnCmd, OrganisePlayerCardsCmd, PickUpACardFromDeckCmd, PickUpCardsFromDiscardPileCmd, PickUpMazzettoDeckCmd, StartGameCmd}
import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoId
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.ended.BurracoGameEnded
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.{GameRepositoryPort, PlayerPort}
import com.abaddon83.cardsGames.burracoGames.queries.{FindBurracoGameEndedQuery, FindBurracoGameInitiatedQuery, FindBurracoGameQuery, FindBurracoGameStartedTurnStartQuery, FindBurracoGameWaitingQuery}
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.CommandDispatcher
import com.abaddon83.libs.cqs.queries.QueryDispatcher

import scala.concurrent.Future


class BurracoGameService(
                          playerPort: PlayerPort,
                          commandDispatcher: CommandDispatcher,
                          queryDispatcher: QueryDispatcher
                        )(implicit val ec: scala.concurrent.ExecutionContext /* = scala.concurrent.ExecutionContext.global*/) {

  def findBurracoGame(gameIdentity: GameIdentity): Future[BurracoGame] = {
    val query = FindBurracoGameQuery(gameIdentity)
    queryDispatcher.dispatchAsync[FindBurracoGameQuery, BurracoGame](query)
  }

  def createNewBurracoGame(): Future[BurracoGameWaitingPlayers] = {
    val gameIdentity = GameIdentity()
    val command = CreateNewBurracoGameCmd(gameIdentity)
    val query = FindBurracoGameWaitingQuery(gameIdentity)
    commandDispatcher.dispatch[CreateNewBurracoGameCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers](query)

  }

  def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    for{
      player <- playerPort.findPlayerNotAssignedBy(playerIdentity)
      command = AddPlayerCmd(gameIdentity, player)
    } yield commandDispatcher.dispatch[AddPlayerCmd](command)

    val query = FindBurracoGameWaitingQuery(gameIdentity)

    queryDispatcher.dispatchAsync[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers](query)
  }

  def startGame(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart] = {
    val command = StartGameCmd(gameIdentity)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[StartGameCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnStart](query)
  }

  def pickUpACardFromDeck(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameInitiatedTurnExecution] = {
    val command = PickUpACardFromDeckCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[PickUpACardFromDeckCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def pickUpCardsFromDiscardPile(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): Future[BurracoGameInitiatedTurnExecution] = {
    val command = PickUpCardsFromDiscardPileCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[PickUpCardsFromDiscardPileCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def organisePlayerCard(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, orderedCards: List[Card]): Future[BurracoGameInitiated] = {
    val command = OrganisePlayerCardsCmd(gameIdentity, playerIdentity,orderedCards)
    val query = FindBurracoGameInitiatedQuery(gameIdentity)

    commandDispatcher.dispatch[OrganisePlayerCardsCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameInitiatedQuery, BurracoGameInitiated](query)
  }

  def dropScale(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, scale: BurracoScale) : Future[BurracoGameInitiatedTurnExecution] = {
    val command = DropScaleCmd(gameIdentity, playerIdentity,scale)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[DropScaleCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def dropTris(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, tris: BurracoTris) : Future[BurracoGameInitiatedTurnExecution] = {
    val command = DropTrisCmd(gameIdentity, playerIdentity,tris)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[DropTrisCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def appendCardOnBurraco(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, burracoId: BurracoId, cardsToAppend: List[Card]) : Future[BurracoGameInitiatedTurnExecution] = {
    val command = AppendCardOnBurracoCmd(gameIdentity, playerIdentity,burracoId,cardsToAppend)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[AppendCardOnBurracoCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnExecution](query)
  }

  def pickUpMazzettoDeck(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity) : Future[BurracoGameInitiated] = {
    val command = PickUpMazzettoDeckCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameInitiatedQuery(gameIdentity)

    commandDispatcher.dispatch[PickUpMazzettoDeckCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameInitiatedQuery, BurracoGameInitiated](query)
  }

  def dropCardOnDiscardPile(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, card: Card) : Future[BurracoGameInitiatedTurnEnd] = {
    val command = DropCardOnDiscardPileCmd(gameIdentity, playerIdentity,card)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[DropCardOnDiscardPileCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery,  BurracoGameInitiatedTurnEnd](query)
  }

  def endPlayerTurn(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): Future[BurracoGameInitiatedTurnStart] = {
    val command = EndPlayerTurnCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameStartedTurnStartQuery(gameIdentity)

    commandDispatcher.dispatch[EndPlayerTurnCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameStartedTurnStartQuery,  BurracoGameInitiatedTurnStart](query)
  }

  def endGame(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): Future[BurracoGameEnded] = {
    val command = EndGameCmd(gameIdentity, playerIdentity)
    val query = FindBurracoGameEndedQuery(gameIdentity)

    commandDispatcher.dispatch[EndGameCmd](command)
    queryDispatcher.dispatchAsync[FindBurracoGameEndedQuery,  BurracoGameEnded](query)
  }


}
