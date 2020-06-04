package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.games.{Game, GameIdentity}


trait BurracoGame extends Game {
  val maxPlayers: Int = 4
  val minPlayers: Int = 2
  val players: List[BurracoPlayer]

}

case class BurracoGameWaitingPlayers(
    override val gameIdentity: GameIdentity,
    override val players: List[BurracoPlayer]
  ) extends BurracoGame{

  def addPlayer(player: PlayerNotAssigned): BurracoGameWaitingPlayers ={
      assert(players.size<maxPlayers,s"Maximum number of players reached, (Max: ${maxPlayers})")
      assert(isAlreadyAPlayer(player) == false, s"The player ${player.playerIdentity.toString()} is already a player of game ${this.gameIdentity.toString()}")
      BurracoGameWaitingPlayers(gameIdentity,List(players,List(player)).flatten)
  }

  def isAlreadyAPlayer(player: BurracoPlayer): Boolean = {
    players.exists(p => p == player)
  }

  def initiate(dealer: BurracoDealer) : Boolean = ???
  /*{
    assert(players.size >1, s"Not enough players to initiate the game, ( Min: ${minPlayers})")
    //assert(dealer.game.gameIDentity == gameIdentity)

  }*/

}

object BurracoGame {
  def createNewBurracoGame(): BurracoGameWaitingPlayers ={
    BurracoGameWaitingPlayers(GameIdentity(),List.empty)
  }
}
