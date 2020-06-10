package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.{Card, Ranks}



case class BurracoTris protected(
                                  override protected val trisId: TrisId,
                                  override protected val rank: Rank,
                                  override protected val cards: List[Card]
                                ) extends Tris {

  def addCards(cardsToAdd: List[Card]): BurracoTris = {
    val updatedCards = validateTris(List(cards,cardsToAdd).flatten)
    copy(cards = updatedCards)
  }

}

object BurracoTris{
  def apply(cards: List[Card]): BurracoTris = {
    assert(cards.size >=3, "A Scale is composed by 3 or more cards")
    val rank = cards.filterNot(c => c.rank == Ranks.Jolly || c.rank == Ranks.Two).head.rank
    val tris=BurracoTris(TrisId(),rank,cards)
    tris.validateTris(tris.showCards)
    tris
  }

}