package com.abaddon83.cardsGames.shares.games

import com.abaddon83.cardsGames.shares.players.Player

trait Game {
  protected val gameIdentity: GameIdentity
  protected val maxPlayers: Int
  protected val minPlayers: Int
  protected val totalCardsRequired: Int
  protected val players: List[Player]

  def numPlayers: Int = {
    players.size
  }

  def listOfPlayers(): List[Player]= {
    players
  }

  def identity(): GameIdentity ={
    gameIdentity
  }
}
