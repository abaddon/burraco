package com.abaddon83.burracoGame.writeModel.models.burracoGameendeds

import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.BurracoPlayer
import com.abaddon83.burracoGame.writeModel.models.MazzettoDecks
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.es.UnsupportedEventException

data class BurracoGameEnded(
        override val identity: GameIdentity,
        override val players: List<BurracoPlayer>,
        val mazzettoMissed: Boolean) : BurracoGame(identity,"BurracoGameEnded") {

    override fun applyEvent(event: Event): BurracoGameEnded {
        log.info("apply event: ${event::class.simpleName.toString()}")
        return when (event) {
            //is GameStarted -> apply(event)
            //is NewTurnStarted -> apply(event)
            else -> throw UnsupportedEventException(event::class.java)
        }
    }

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