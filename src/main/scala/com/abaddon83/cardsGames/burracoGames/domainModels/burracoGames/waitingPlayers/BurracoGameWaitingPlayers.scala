package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoPlayer
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.cardsGames.burracoGames.services.BurracoDealerFactory
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class BurracoGameWaitingPlayers(
                                      override val gameIdentity: GameIdentity,
                                      override protected val players: List[BurracoPlayer]
                                    ) extends BurracoGame{

  def addPlayer(player: BurracoPlayer): BurracoGameWaitingPlayers ={
    assert(players.size < maxPlayers,s"Maximum number of players reached, (Max: ${maxPlayers})")
    assert(isAlreadyAPlayer(player.playerIdentity) == false, s"The player ${player.playerIdentity.toString()} is already a player of game ${this.gameIdentity.toString()}")
    BurracoGameWaitingPlayers(gameIdentity,players ++ List(player))
  }

  def isAlreadyAPlayer(playerIdentity: PlayerIdentity): Boolean = {
    players.exists(p => p.playerIdentity == playerIdentity)
  }

  def start(): BurracoGameInitiatedTurnStart = {
    assert(players.size >1, s"Not enough players to initiate the game, ( Min: ${minPlayers})")
    val burracoCardsDealt = BurracoDealerFactory(this).dealBurracoCards()
    BurracoGameInitiatedTurnStart.build(this,burracoCardsDealt).testInvariants()
  }

}
