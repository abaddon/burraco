package com.abaddon83.burraco.`match`.games.ports

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burraco.shares.games.{Game, GameIdentity}

import scala.concurrent.Future

trait GameRepositoryPort {

  def save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers
  def save(burracoGame: BurracoGameInitiatedTurnStart): BurracoGameInitiatedTurnStart
  def exists(gameIdentity: GameIdentity): Boolean
  def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers]
  def findBurracoGameInitialisedBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart]
  def findAllBurracoGameWaitingPlayers(): Future[List[BurracoGameWaitingPlayers]]
}
