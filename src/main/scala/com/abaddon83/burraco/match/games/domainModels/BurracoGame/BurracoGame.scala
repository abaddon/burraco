package com.abaddon83.burraco.`match`.games.domainModels.BurracoGame

import com.abaddon83.burraco.`match`.games.domainModels._
import com.abaddon83.burraco.shares.games.{Game, GameIdentity}

trait BurracoGame extends Game {
  protected val maxPlayers: Int = 4
  protected val minPlayers: Int = 2
  protected val totalCardsRequired: Int = 108
  protected val players: List[BurracoPlayer]

  def numPlayers: Int = {
    players.size
  }

  def listOfPlayers(): List[BurracoPlayer]= {
    players
  }
}

object BurracoGame {
  def createNewBurracoGame(): BurracoGameWaitingPlayers ={
    BurracoGameWaitingPlayers(GameIdentity(),List.empty)
  }
}



