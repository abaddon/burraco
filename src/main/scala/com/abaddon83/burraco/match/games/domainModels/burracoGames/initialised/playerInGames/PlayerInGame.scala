package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId, BurracoPlayer}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class PlayerInGame(
                                playerIdentity: PlayerIdentity,
                                cards: List[Card],
                                cardsOnTable: BurracoCardsOnTable,
                                pozzettoTaken: Boolean = false
                              ) extends BurracoPlayer {
//pickup
  def addPozzettoOnMyCard(pozzetto: List[Card]):PlayerInGame = {
    copy(pozzettoTaken = true, cards = this.cards ++ pozzetto)
  }

  def dropATris(tris: BurracoTris): PlayerInGame = {
    val updatedPlayerCards = cards diff tris.showCards
    val updatedPlayerCardsOnTable = cardsOnTable.addTris(tris)

    this.copy( cards = updatedPlayerCards,cardsOnTable = updatedPlayerCardsOnTable)

  }

  def dropAScale(scale: BurracoScale): PlayerInGame = {
    val updatedPlayerCards = cards diff scale.showCards
    val updatedPlayerCardsOnTable = cardsOnTable.addScale(scale)

    this.copy( cards = updatedPlayerCards,cardsOnTable = updatedPlayerCardsOnTable)

  }

  def dropACard(card:Card): PlayerInGame = {
    assert(cards.exists( c => c == card),s"This card ${card} is not a card of the player ${this}")
    this.copy(cards = cards diff List(card))
  }

  def appendACardOnBurracoDropped(burracoId: BurracoId,cardsToAppend: List[Card]): PlayerInGame ={
    //add the cards to an existing burraco
    val updatedPlayerCardsOnTable =  cardsOnTable.appendCardOnBurraco(burracoId,cardsToAppend)
    //remove the cards attached from thr player cards.
    val updatedPlayerCards = cards diff cardsToAppend
    this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
  }

  def addNewCardsOnMyCard(newCards: List[Card]): PlayerInGame = {
    this.copy( cards = cards ++ newCards)
  }

  //def updateCardsOnTable(updatedCardsOnTable: BurracoCardsOnTable): PlayerInGame = {
  //  this.copy( cardsOnTable = updatedCardsOnTable)
  //}

  def totalPlayerCards(): Int = {
    cards.size + cardsOnTable.numCardsOnTable()
  }

}

object PlayerInGame{
  def apply(playerIdentity: PlayerIdentity, cards: List[Card]): PlayerInGame = {
    new PlayerInGame(playerIdentity, cards,BurracoCardsOnTable(List.empty,List.empty))
  }
}
