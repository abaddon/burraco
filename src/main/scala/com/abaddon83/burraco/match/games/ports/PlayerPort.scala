package com.abaddon83.burraco.`match`.games.ports

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.burraco.`match`.games.domainModels.{BurracoPlayer, PlayerNotAssigned}
import com.abaddon83.burraco.shares.players.PlayerIdentity

import scala.concurrent.Future

trait PlayerPort {
  def findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): Future[PlayerNotAssigned]
  def findBurracoPlayerBy(playerIdentity: PlayerIdentity): Future[BurracoPlayer]
  def findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): Future[PlayerInGame]
}
