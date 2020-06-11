package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.BurracoPlayer
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class PlayerInGame(
                                playerIdentity: PlayerIdentity,
                                cards: List[Card],
                                cardsOnTable: BurracoCardsOnTable
                              ) extends BurracoPlayer {

  def updateCards(updatedCards: List[Card]): PlayerInGame = {
    this.copy( cards = updatedCards)
  }

  def updateCardsOnTable(updatedCardsOnTable: BurracoCardsOnTable): PlayerInGame = {
    this.copy( cardsOnTable = updatedCardsOnTable)
  }

}

object PlayerInGame{
  def apply(playerIdentity: PlayerIdentity, cards: List[Card]): PlayerInGame = {
    new PlayerInGame(playerIdentity, cards,BurracoCardsOnTable(List.empty,List.empty))
  }
}
