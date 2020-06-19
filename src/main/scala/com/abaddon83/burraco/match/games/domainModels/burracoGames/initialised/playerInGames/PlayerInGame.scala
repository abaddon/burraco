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

  def appendACardOnBurracoDropped(burracoId: BurracoId,cardsToAppend: List[Card]): PlayerInGame ={
    if(cardsOnTable.listOfScale.find(s => s.getBurracoId() == burracoId).isDefined){
      appendACardOnScaleDropped(burracoId,cardsToAppend)
    }else if(cardsOnTable.listOfTris.find(t => t.getBurracoId() == burracoId).isDefined){
      appendACardOnTrisDropped(burracoId,cardsToAppend)
    }else{
      throw new NoSuchElementException(s"The ${burracoId} doesn't exist")
    }
  }

  private def appendACardOnScaleDropped(burracoId: BurracoId,cardsToAppend: List[Card]): PlayerInGame ={
    val updatedPlayerCardsOnTable = cardsOnTable.updateScale(burracoId,cardsToAppend)
    val updatedPlayerCards = cards diff cardsToAppend

    this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
  }

  private def appendACardOnTrisDropped(burracoId: BurracoId,cardsToAppend: List[Card]): PlayerInGame ={
    val updatedPlayerCardsOnTable = cardsOnTable.updateTris(burracoId,cardsToAppend)
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
