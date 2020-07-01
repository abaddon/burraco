package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.ended

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.MazzettoDecks
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class BurracoGameEnded(
                                override protected val gameIdentity: GameIdentity,
                                override protected val players: List[PlayerScore],
                                pozzettoMissed: Boolean
                              ) extends BurracoGame {

}


object BurracoGameEnded {
  def build(gameIdentity: GameIdentity, players: List[PlayerInGame], pozzettos: MazzettoDecks, playerTurn: PlayerIdentity): BurracoGameEnded = {
    BurracoGameEnded(
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
