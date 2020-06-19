package com.abaddon83.burraco.`match`.games.ports

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.completed.BurracoGameCompleted
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burraco.shares.games.{Game, GameIdentity}

import scala.concurrent.Future

trait GameRepositoryPort {

  def save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers
  def save(burracoGame: BurracoGameInitiatedTurnStart): BurracoGameInitiatedTurnStart
  def save(burracoGame: BurracoGameInitiatedTurnExecution): BurracoGameInitiatedTurnExecution
  def save(burracoGame: BurracoGameInitiatedTurnEnd): BurracoGameInitiatedTurnEnd
  def save(burracoGame: BurracoGameInitiated): BurracoGameInitiated
  def save(burracoGame: BurracoGameCompleted): BurracoGameCompleted
  def exists(gameIdentity: GameIdentity): Boolean
  def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers]
  def findBurracoGameInitialisedBy(gameIdentity: GameIdentity): Future[BurracoGameInitiated]
  def findBurracoGameInitialisedTurnStartBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart]
  def findBurracoGameInitialisedTurnExecutionBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnExecution]
  def findBurracoGameInitialisedTurnEndBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnEnd]
  def findAllBurracoGameWaitingPlayers(): Future[List[BurracoGameWaitingPlayers]]
}
