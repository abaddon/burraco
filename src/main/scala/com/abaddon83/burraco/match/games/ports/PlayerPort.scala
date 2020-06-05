package com.abaddon83.burraco.`match`.games.ports

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoPlayerAssigned, BurracoPlayerInGame, PlayerNotAssigned}
import com.abaddon83.burraco.shares.players.PlayerIdentity

import scala.concurrent.Future

trait PlayerPort {
  def findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): Future[PlayerNotAssigned]
  def findBurracoPlayerAssignedBy(playerIdentity: PlayerIdentity): Future[BurracoPlayerAssigned]
  def findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): Future[BurracoPlayerInGame]
}
