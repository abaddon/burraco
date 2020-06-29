package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.Card

case class MazzettoDecks protected(protected val list: List[MazzettoDeck]){

  def firstMazzettoAvailable(): MazzettoDeck = {
    assert(!list.isEmpty, "Mazzetto list empty, all Mazzetto taken")
    /*list.find(p => p.numCards() > 0) match {
      case Some(pozzettoDeck) => pozzettoDeck.grabAllCards()
      case None => throw new NoSuchElementException("No pottezzi available")
    }*/
    list.head
  }

  def mazzettoTaken(mazzettoDeck: MazzettoDeck): MazzettoDecks = {
    assert(!list.exists(m => m == mazzettoDeck), "Mazzettp not found")
    copy(
      list = list.filterNot( m => m == mazzettoDeck)
    )
  }

  def numCards(): Int ={
    list.map(m => m.numCards()).foldLeft(0)(_ + _)
  }

}

object MazzettoDecks{
  def build(list: List[MazzettoDeck]): MazzettoDecks = {
    assert(list.size == 2)
    MazzettoDecks(list.sortBy(p=> p.numCards()).reverse)
  }
}
