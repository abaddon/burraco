package com.abaddon83.burraco.`match`.games.ports

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burraco.shares.games.{Game, GameIdentity}

import scala.concurrent.Future

trait GameRepositoryPort {

  def save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers
  def save(burracoGame: BurracoGameInitiatedTurnStart): BurracoGameInitiatedTurnStart
  def save(burracoGame: BurracoGameInitiatedTurnExecution): BurracoGameInitiatedTurnExecution
  def save(burracoGame: BurracoGameInitiatedTurnEnd): BurracoGameInitiatedTurnEnd
  def exists(gameIdentity: GameIdentity): Boolean
  def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers]
  def findBurracoGameInitialisedTurnStartBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart]
  def findBurracoGameInitiatedTurnExecutionBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnExecution]
  def findBurracoGameInitiatedTurnEndBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnEnd]
  def findAllBurracoGameWaitingPlayers(): Future[List[BurracoGameWaitingPlayers]]
}
