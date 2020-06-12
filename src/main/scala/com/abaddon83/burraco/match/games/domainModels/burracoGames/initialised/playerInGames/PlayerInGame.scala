package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoPlayer, ScaleId, TrisId}
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
    val updatedPlayerCardsOnTable = cardsOnTable.updateListOfTris(tris :: cardsOnTable.listOfTris)

    this.copy( cards = updatedPlayerCards,cardsOnTable = updatedPlayerCardsOnTable)

  }

  def dropAScale(scale: BurracoScale): PlayerInGame = {
    val updatedPlayerCards = cards diff scale.showCards
    val updatedPlayerCardsOnTable = cardsOnTable.updateListOfScale(scale :: cardsOnTable.listOfScale)

    this.copy( cards = updatedPlayerCards,cardsOnTable = updatedPlayerCardsOnTable)

  }

  def dropACard(card:Card): PlayerInGame = {
    assert(cards.exists( c => c == card),s"This card ${card} is not a card of the player ${this}")
    this.copy(cards = cards diff List(card))
  }

  def appendACardOnScaleDropped(scaleId: ScaleId,cardsToAppend: List[Card]): PlayerInGame ={
    val updatedPlayerCardsOnTable = cardsOnTable.updateScale(scaleId,cardsToAppend)
    val updatedPlayerCards = cards diff cardsToAppend

    this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
  }

  def appendACardOnTrisDropped(trisId: TrisId,cardsToAppend: List[Card]): PlayerInGame ={
    val updatedPlayerCardsOnTable = cardsOnTable.updateTris(trisId,cardsToAppend)
    val updatedPlayerCards = cards diff cardsToAppend

    this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
  }

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
