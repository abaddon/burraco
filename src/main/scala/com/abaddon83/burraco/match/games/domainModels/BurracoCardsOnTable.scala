package com.abaddon83.burraco.`match`.games.domainModels

case class BurracoCardsOnTable(
                              listOfTris: List[Tris],
                              listOfScale: List[Scale]
                              ) {
  def updateListOfTris(updatedListOfTris: List[Tris]): BurracoCardsOnTable = {
    this.copy( listOfTris = updatedListOfTris)
  }

  def updateListOfScale(updatedListOfScale: List[Scale]): BurracoCardsOnTable = {
    this.copy( listOfScale = updatedListOfScale)
  }
}

