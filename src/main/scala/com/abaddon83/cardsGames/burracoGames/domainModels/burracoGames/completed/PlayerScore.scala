package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.completed

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoPlayer
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{Burraco, PlayerInGame}
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class PlayerScore(
                                playerIdentity: PlayerIdentity,
                                winner: Boolean,
                                burracoList: List[BurracoPoint],
                                remainedCards: List[Card]
                              ) extends BurracoPlayer {

}

object PlayerScore{
  def build(player: PlayerInGame,winner: Boolean): PlayerScore = {
    PlayerScore(
    playerIdentity = player.playerIdentity,
    winner = winner,
    burracoList = burracoList(player.burracoList()),
    remainedCards = player.showMyCards()
    )
  }

  def burracoList(burracoList: List[Burraco]): List[BurracoPoint] ={
    burracoList.map(burraco =>
      BurracoPoint(burraco)
    )
  }
}


