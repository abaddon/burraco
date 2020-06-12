package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId}
import com.abaddon83.burraco.shares.decks.Card

case class BurracoCardsOnTable(
                                listOfTris: List[BurracoTris],
                                listOfScale: List[BurracoScale]
                              ) {
  def burracoList(): List[Burraco] ={
    listOfTris.filter(t => t.isBurraco()) ++
    listOfScale.filter(s => s.isBurraco())
  }

  def updateListOfTris(updatedListOfTris: List[BurracoTris]): BurracoCardsOnTable = {
    this.copy( listOfTris = updatedListOfTris)
  }

  def updateListOfScale(updatedListOfScale: List[BurracoScale]): BurracoCardsOnTable = {
    this.copy( listOfScale = updatedListOfScale)
  }

  def updateScale(burracoId: BurracoId, cardsToAppend: List[Card]): BurracoCardsOnTable = {
    val listOfBurracoScale= listOfScale.map(scale =>
      if(scale.getBurracoId() == burracoId){
        scale.addCards(cardsToAppend)
      }else {
        scale
      }
    )
    copy(listOfScale = listOfBurracoScale)
  }

  def updateTris(burracoId: BurracoId, cardsToAppend: List[Card]): BurracoCardsOnTable = {
    val listOfBurracoTris= listOfTris.map(tris =>
      if(tris.getBurracoId() == burracoId){
        tris.addCards(cardsToAppend)
      }else {
        tris
      }
    )
    copy(listOfTris = listOfBurracoTris)
  }

}

