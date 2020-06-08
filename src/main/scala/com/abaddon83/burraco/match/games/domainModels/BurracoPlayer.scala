package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.{Player, PlayerIdentity}

trait BurracoPlayer extends Player{
}


case class PlayerNotAssigned(playerIdentity: PlayerIdentity) extends BurracoPlayer {

}

case class BurracoPlayerAssigned(playerIdentity: PlayerIdentity) extends BurracoPlayer {

}

case class BurracoPlayerInGame(
                                playerIdentity: PlayerIdentity,
                                cards: List[Card],
                                cardsOnTable: BurracoCardsOnTable
                              ) extends BurracoPlayer {

  def updateCards(updatedCards: List[Card]): BurracoPlayerInGame = {
    this.copy( cards = updatedCards)
  }

  def updateCardsOnTable(updatedCardsOnTable: BurracoCardsOnTable): BurracoPlayerInGame = {
    this.copy( cardsOnTable = updatedCardsOnTable)
  }

}

object BurracoPlayerInGame{
  def apply(playerIdentity: PlayerIdentity, cards: List[Card]): BurracoPlayerInGame = {
    new BurracoPlayerInGame(playerIdentity, cards,BurracoCardsOnTable(List.empty,List.empty))
  }
}