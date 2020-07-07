package com.abaddon83.cardsGames.burracoGames.ports

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

import scala.concurrent.Future

trait BurracoGameUIPort {

  def createNewBurracoGame(): Future[BurracoGameWaitingPlayers]
  def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers]
  def findBurracoGame(gameIdentity: GameIdentity): Future[BurracoGame]


}
