package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

case class Pozzettos protected (protected val list: List[PozzettoDeck]){

  def firstPozzettoAvailable(): PozzettoDeck = {
    if(list.isEmpty){
      throw new NoSuchFieldException("No pottezzi available")
    }
    list.head
  }
}

object Pozzettos{
  def build(list: List[PozzettoDeck]): Pozzettos = {
    assert(list.size == 2)
    Pozzettos(list.sortBy( p=> p.numCards()))
  }
}
