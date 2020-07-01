package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoPlayer
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.shares.games.{Game, GameIdentity}

trait BurracoGame extends Game {
  protected val maxPlayers: Int = 4
  protected val minPlayers: Int = 2
  protected val totalCardsRequired: Int = 108
  override protected val players: List[BurracoPlayer]



  override def listOfPlayers(): List[BurracoPlayer]= {
    players
  }
}

object BurracoGame {
  def createNewBurracoGame(gameIdentity: GameIdentity): BurracoGameWaitingPlayers ={
    BurracoGameWaitingPlayers(gameIdentity,List.empty)
  }
}



