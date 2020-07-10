package com.abaddon83.burracoGame.domainModels.burracoGameendeds

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.BurracoPlayer
import com.abaddon83.burracoGame.domainModels.MazzettoDecks
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class BurracoGameEnded(
        override val players: List<BurracoPlayer>,
        override val identity: GameIdentity,
        val mazzettoMissed: Boolean) : BurracoGame() {

    companion object Factory {
        fun create(identity: GameIdentity, players: List<PlayerInGame>, pozzettos: MazzettoDecks, playerTurn: PlayerIdentity): BurracoGameEnded =
            BurracoGameEnded(
                    identity = identity,
                    players = playersScore(players, playerTurn),
                    mazzettoMissed = pozzettos.numCards() > 0
            )

        private fun playersScore(players: List<PlayerInGame>, winner: PlayerIdentity): List<PlayerScore> =
                players.map { p ->
                    PlayerScore.create(p, p.identity() == winner)
                }

    }

}


/*

case class BurracoGameEnded(
                             override protected val gameIdentity: GameIdentity,
                             override protected val players: List[PlayerScore],
                             mazzettoMissed: Boolean
                              ) extends BurracoGame {

}


object BurracoGameEnded {
  def build(gameIdentity: GameIdentity, players: List[PlayerInGame], pozzettos: MazzettoDecks, playerTurn: PlayerIdentity): BurracoGameEnded = {
    BurracoGameEnded(
      gameIdentity = gameIdentity,
      players = playersScore(players, playerTurn),
      mazzettoMissed = pozzettos.numCards() > 0
    )
  }

  def playersScore(players: List[PlayerInGame], winner: PlayerIdentity): List[PlayerScore] = {
    players.map(p =>
      PlayerScore.build(p, p.playerIdentity == winner)
    )
  }


}

 */