package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.Pozzettos
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameInitiatedTurnEnd protected(
                                        override protected val players: List[PlayerInGame],
                                        override protected val playerTurn: PlayerIdentity,
                                        override protected val burracoDeck: BurracoDeck,
                                        override protected val pozzettos: Pozzettos,
                                        override protected val discardPile: DiscardPile,
                                        override protected val gameIdentity: GameIdentity
                                      ) extends BurracoGameInitiated{

}


object BurracoGameInitiatedTurnEnd{
  def build(players: List[PlayerInGame], playerTurn: PlayerIdentity, burracoDeck: BurracoDeck, pozzettos: Pozzettos, discardPile: DiscardPile, gameIdentity: GameIdentity): BurracoGameInitiatedTurnEnd = {
    new BurracoGameInitiatedTurnEnd(players, playerTurn, burracoDeck, pozzettos, discardPile, gameIdentity)
  }
}

