package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameUIPort
import com.abaddon83.cardsGames.burracoGames.services.BurracoGameService
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

import scala.concurrent.Future

class BurracoGameUIAkkaAdapter(burracoGameService: BurracoGameService) extends BurracoGameUIPort{
  override def createNewBurracoGame(): Future[BurracoGameWaitingPlayers] = ???

  override def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = ???

  override def findBurracoGame(gameIdentity: GameIdentity): Future[BurracoGame] = {
    burracoGameService.findBurracoGame(gameIdentity)
  }
}
