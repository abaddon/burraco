package com.abaddon83.cardsGames.burracoGames.ports

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.cardsGames.burracoGames.domainModels.{BurracoPlayer, PlayerNotAssigned}
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

import scala.concurrent.Future

trait PlayerPort {
  def findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): Future[PlayerNotAssigned]
  def findBurracoPlayerBy(playerIdentity: PlayerIdentity): Future[BurracoPlayer]
  def findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): Future[PlayerInGame]
}
