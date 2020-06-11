package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.Card

case class Pozzettos protected (protected val list: List[PozzettoDeck]){

  def firstPozzettoAvailable(): List[Card] = {
    list.find(p => p.numCards() > 0) match {
      case Some(pozzettoDeck) => pozzettoDeck.grabAllCards()
      case None => throw new NoSuchElementException("No pottezzi available")
    }
  }
  def numCards(): Int ={
    list.map(p => p.numCards()).foldLeft(0)(_ + _)
  }

}

object Pozzettos{
  def build(list: List[PozzettoDeck]): Pozzettos = {
    assert(list.size == 2)
    Pozzettos(list.sortBy( p=> p.numCards()).reverse)
  }
}
