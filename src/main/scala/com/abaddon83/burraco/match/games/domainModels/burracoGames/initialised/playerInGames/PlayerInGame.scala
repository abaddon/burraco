package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.mazzettos.MazzettoDeck
import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId, BurracoPlayer}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class PlayerInGame protected(
                         playerIdentity: PlayerIdentity,
                         protected val cards: List[Card],
                         protected val cardsOnTable: BurracoCardsOnTable,
                         mazzettoTaken: Boolean = false
                              ) extends BurracoPlayer {

  def showMyCards() : List[Card] ={
    cards
  }

  def showTrisOnTable(): List[BurracoTris] = {
    cardsOnTable.showTris()
  }

  def showScalesOnTable(): List[BurracoScale] = {
    cardsOnTable.showScale()
  }

  def burracoList(): List[Burraco] = {
    cardsOnTable.burracoList()
  }

  def orderPlayerCards(cardsOrdered: List[Card]): PlayerInGame ={
    assert(cardsOrdered.sorted == cards.sorted,"The cardsOrdered doesn't contain the same player cards")
    copy(cards = cardsOrdered)
  }

  //pickup
  def pickUpMazzetto(mazzetto: MazzettoDeck):PlayerInGame = {
    assert(mazzettoTaken == false, MazzettoDeck)
    addCardsOnMyCard(mazzetto.getCards()).copy(mazzettoTaken = true)
  }

  def addCardsOnMyCard(newCards: List[Card]): PlayerInGame = {
    this.copy( cards = cards ++ newCards)
  }

  def dropATris(tris: BurracoTris): PlayerInGame = {
    val updatedPlayerCards = cards diff tris.showCards
    val updatedPlayerCardsOnTable = cardsOnTable.addTris(tris)
    this.copy( cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
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


  //def updateCardsOnTable(updatedCardsOnTable: BurracoCardsOnTable): PlayerInGame = {
  //  this.copy( cardsOnTable = updatedCardsOnTable)
  //}

  def totalPlayerCards(): Int = {
    cards.size + cardsOnTable.numCardsOnTable()
  }

}

object PlayerInGame{
  def build(playerIdentity: PlayerIdentity, cards: List[Card]): PlayerInGame = {
    new PlayerInGame(playerIdentity, cards,BurracoCardsOnTable(List.empty,List.empty))
  }
}
