package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.burraco.`match`.games.domainModels.{BurracoPlayer, PlayerNotAssigned}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameWaitingPlayers(
                                      override val gameIdentity: GameIdentity,
                                      override protected val players: List[BurracoPlayer]
                                    ) extends BurracoGame{

  def addPlayer(player: PlayerNotAssigned): BurracoGameWaitingPlayers ={
    assert(players.size < maxPlayers,s"Maximum number of players reached, (Max: ${maxPlayers})")
    assert(isAlreadyAPlayer(player.playerIdentity) == false, s"The player ${player.playerIdentity.toString()} is already a player of game ${this.gameIdentity.toString()}")
    BurracoGameWaitingPlayers(gameIdentity,players ++ List(player))
  }

  def isAlreadyAPlayer(playerIdentity: PlayerIdentity): Boolean = {
    players.exists(p => p.playerIdentity == playerIdentity)
  }

  def initiate(burracoCardsDealt: BurracoCardsDealt): BurracoGameInitiatedTurnStart = {
    assert(players.size >1, s"Not enough players to initiate the game, ( Min: ${minPlayers})")
    BurracoGameInitiatedTurnStart.build(this,burracoCardsDealt).testInvariants()
  }

}
