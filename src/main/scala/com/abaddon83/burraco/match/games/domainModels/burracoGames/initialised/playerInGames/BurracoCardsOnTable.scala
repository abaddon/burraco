package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId}
import com.abaddon83.burraco.shares.decks.Card

case class BurracoCardsOnTable(
                                protected val listOfTris: List[BurracoTris],
                                protected val listOfScale: List[BurracoScale]
                              ) {
  def burracoList(): List[Burraco] ={
    listOfTris.filter(t => t.isBurraco()) ++
    listOfScale.filter(s => s.isBurraco())
  }

  def numCardsOnTable():Int = {
      listOfTris.map( tris => tris.showCards.size).foldLeft(0)(_ + _) +
      listOfScale.map( scale => scale.showCards.size).foldLeft(0)(_ + _)
  }

  def addTris(trisToAdd: BurracoTris): BurracoCardsOnTable = {
    this.copy( listOfTris = listOfTris ++ List(trisToAdd))
  }

  def addScale(scaleToAdd: BurracoScale): BurracoCardsOnTable = {
    this.copy( listOfScale = listOfScale ++ List(scaleToAdd))
  }

  def appendCardOnBurraco(burracoId: BurracoId,cardsToAppend: List[Card]): BurracoCardsOnTable ={
    if(listOfScale.exists(s => s.getBurracoId() == burracoId)){
      appendCardOnScale(burracoId,cardsToAppend)
    }else if(listOfTris.exists(t => t.getBurracoId() == burracoId)){
      appendCardOnTris(burracoId,cardsToAppend)
    }else throw new NoSuchElementException(s"The ${burracoId} doesn't exist")
  }

  private def appendCardOnScale(burracoId: BurracoId, cardsToAppend: List[Card]): BurracoCardsOnTable = {
    val listOfBurracoScale= listOfScale.map(scale =>
      if(scale.getBurracoId() == burracoId){
        scale.addCards(cardsToAppend)
      }else {
        scale
      }
    )
    copy(listOfScale = listOfBurracoScale)
  }

  private def appendCardOnTris(burracoId: BurracoId, cardsToAppend: List[Card]): BurracoCardsOnTable = {
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

