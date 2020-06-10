package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card

case class BurracoCardsOnTable(
                              listOfTris: List[Tris],
                              listOfScale: List[BurracoScale]
                              ) {
  def updateListOfTris(updatedListOfTris: List[Tris]): BurracoCardsOnTable = {
    this.copy( listOfTris = updatedListOfTris)
  }

  def updateListOfScale(updatedListOfScale: List[BurracoScale]): BurracoCardsOnTable = {
    this.copy( listOfScale = updatedListOfScale)
  }

  def updateScale(scaleId: ScaleId, cardsToAppend: List[Card]): BurracoCardsOnTable = {
    val listOfBurracoScale= listOfScale.map(scale =>
      if(scale.getScaleId == scaleId){
        scale.addCards(cardsToAppend)
      }else {
        scale
      }
    )
    copy(listOfScale = listOfBurracoScale)
  }

  def updateTris(trisId: TrisId, cardsToAppend: List[Card]): BurracoCardsOnTable = {
    val listOfBurracoTris= listOfTris.map(tris =>
      if(tris.getTrisId() == trisId){
        tris.addCards(cardsToAppend)
      }else {
        tris
      }
    )
    copy(listOfTris = listOfBurracoTris)
  }

  /*
  /*
  player.cardsOnTable.
   */
   */
}

