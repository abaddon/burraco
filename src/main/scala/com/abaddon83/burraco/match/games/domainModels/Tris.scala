package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.decks.Ranks.{Jolly, Rank, Two}

case class Tris private(private val rank: Rank, private val cards: List[Card]) {

  def showCards: List[Card]  = {
    cards
  }
}



object Tris{
  def apply(cards: List[Card]): Tris = {
    validateTris(cards)
    val rank = cards.filterNot(c => c.rank == Jolly || c.rank == Two).head.rank
    new Tris(rank,cards)
  }

  private def validateTris(cards: List[Card]): Unit ={
    assert(cards.size >=3, "A tris is composed by 3 or more cards")
    val cardsWithoutJolly = cards.filterNot(c => c.rank == Jolly || c.rank == Two)
    assert(cardsWithoutJolly.size <2, "A tris is can contain at least 1 Jolly or Two")
    if(cardsWithoutJolly.exists(_.rank != cards.head.rank)){
      throw new IllegalArgumentException("A tris is composed cards with the same rank")
    }

  }
}