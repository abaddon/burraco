package com.abaddon83.burraco.`match`.games.ports

import com.abaddon83.burraco.`match`.games.domainModels.BurracoGame.{BurracoGamePlayerTurnStart, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.shares.games.GameIdentity

import scala.concurrent.Future

trait BurracoGameRepositoryPort {

  def save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers
  def save(burracoGame: BurracoGamePlayerTurnStart): BurracoGamePlayerTurnStart
  def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers]
  def findBurracoGameInitialisedBy(gameIdentity: GameIdentity): Future[BurracoGamePlayerTurnStart]
  def findAllBurracoGameWaitingPlayers(): Future[List[BurracoGameWaitingPlayers]]
}
