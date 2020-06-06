package com.abaddon83.burraco.`match`.games.domainModels.BurracoGame

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoCardsDealt, BurracoPlayer, PlayerNotAssigned}
import com.abaddon83.burraco.shares.games.GameIdentity

case class BurracoGameWaitingPlayers(
                                      override val gameIdentity: GameIdentity,
                                      override protected val players: List[BurracoPlayer]
                                    ) extends BurracoGame{

  def addPlayer(player: PlayerNotAssigned): BurracoGameWaitingPlayers ={
    assert(players.size < maxPlayers,s"Maximum number of players reached, (Max: ${maxPlayers})")
    assert(isAlreadyAPlayer(player) == false, s"The player ${player.playerIdentity.toString()} is already a player of game ${this.gameIdentity.toString()}")
    BurracoGameWaitingPlayers(gameIdentity,List(players,List(player)).flatten)
  }

  def isAlreadyAPlayer(player: BurracoPlayer): Boolean = {
    players.exists(p => p == player)
  }

  def initiate(burracoCardsDealt: BurracoCardsDealt): BurracoGameInitialised = {

    assert(players.size >1, s"Not enough players to initiate the game, ( Min: ${minPlayers})")
    //assert(players.exists(p => p.playerIdentity == playerRequestedToStart),"Only the participants can initiate the game")

    BurracoGameInitialised(this,burracoCardsDealt)
  }

}
