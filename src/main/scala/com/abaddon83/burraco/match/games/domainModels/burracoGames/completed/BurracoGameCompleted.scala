package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.completed

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.MazzettoDecks
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameCompleted(
                                override protected val gameIdentity: GameIdentity,
                                override protected val players: List[PlayerScore],
                                pozzettoMissed: Boolean
                              ) extends BurracoGame {

}


object BurracoGameCompleted {
  def build(gameIdentity: GameIdentity, players: List[PlayerInGame], pozzettos: MazzettoDecks, playerTurn: PlayerIdentity): BurracoGameCompleted = {
    BurracoGameCompleted(
      gameIdentity = gameIdentity,
      players = playersScore(players, playerTurn),
      pozzettoMissed = pozzettos.numCards() > 0
    )
  }

  def playersScore(players: List[PlayerInGame], winner: PlayerIdentity): List[PlayerScore] = {
    players.map(p =>
      PlayerScore.build(p, p.playerIdentity == winner)
    )
  }


}
