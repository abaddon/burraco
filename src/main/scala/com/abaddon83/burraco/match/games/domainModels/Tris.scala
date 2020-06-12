package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.{Card, Ranks}




trait Tris {
  protected val rank: Rank
  protected val cards: List[Card]

  def addCards(cardsToAdd: List[Card]): Tris


  protected def validateTris(cards: List[Card]) ={
    assert(cards.size >=3, "A tris is composed by 3 or more cards")
    val cardsWithoutJolly = cards.filterNot(c => c.rank == Ranks.Jolly || c.rank == Ranks.Two)

    assert((cards diff cardsWithoutJolly).size <= 1, "A tris can contain at least 1 Jolly or Two")
    if(cardsWithoutJolly.exists(_.rank != cards.head.rank)){
      throw new IllegalArgumentException("A tris is composed cards with the same rank")
    }
    cards
  }
}
