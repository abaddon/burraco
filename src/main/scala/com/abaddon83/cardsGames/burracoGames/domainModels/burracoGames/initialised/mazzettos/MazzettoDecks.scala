package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos

import com.abaddon83.cardsGames.shares.decks.Card

case class MazzettoDecks protected(protected val list: List[MazzettoDeck]){

  def firstMazzettoAvailable(): MazzettoDeck = {
    assert(!list.isEmpty, "Mazzetto list empty, all Mazzetto taken")
    list.head
  }

  def mazzettoTaken(mazzettoDeck: MazzettoDeck): MazzettoDecks = {
    assert(list.exists(m => m == mazzettoDeck), "MazzettoDeck not found")
    copy(
      list = list diff List(mazzettoDeck)
    )
  }

  def numCards(): Int ={
    list.map(m => m.numCards()).foldLeft(0)(_ + _)
  }
}

object MazzettoDecks{
  def build(list: List[MazzettoDeck]): MazzettoDecks = {
    assert(list.size == 2,"MazzettoDecks can accept only 2 MazzettoDecks")
    MazzettoDecks(list.sortBy(p=> p.numCards()).reverse)
  }
}
