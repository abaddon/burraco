package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId, Tris}
import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.{Card, Ranks}



case class BurracoTris protected(
                                  override protected val burracoId: BurracoId,
                                  override protected val rank: Rank,
                                  override protected val cards: List[Card]
                                ) extends Burraco with Tris {

  def addCards(cardsToAdd: List[Card]): BurracoTris = {
    validateNewCards(cardsToAdd)
  }

  override def showCards(): List[Card] = cards

  protected def validateNewCards(cardsToAdd: List[Card]): BurracoTris = {
    val tmpCardList=cardsToAdd ++ this.cards
    val cardsWithoutJolly = tmpCardList.filterNot(c => c.rank == Ranks.Jolly || c.rank == Ranks.Two)
    assert((tmpCardList diff cardsWithoutJolly).size <= 1, "A tris can contain at least 1 Jolly or Two")
    assert(cardsWithoutJolly.exists(_.rank != rank) == false,"A tris is composed cards with the same rank")
    copy(cards = tmpCardList)
  }

}

object BurracoTris{
  def apply(cards: List[Card]): BurracoTris = {
    assert(cards.size >=3, "A tris is composed by 3 or more cards")
    val rank = calculateTrisRank(cards)
    assert(!List(Ranks.Two,Ranks.Jolly).contains(rank), "Tris of Jolly or Two are not allowed")
    BurracoTris(BurracoId(),rank,cards)
  }

  private def calculateTrisRank(cards: List[Card]): Rank ={
    val cardsByRank=cards.groupMapReduce(c => c.rank)(_ => 1)(_ + _)
    val cardsByRankWithoutJollyAndTwo=cardsByRank.removed(Ranks.Jolly).removed(Ranks.Two)

    assert(cardsByRankWithoutJollyAndTwo.keySet.size ==1,s"Too many different ranks found: ${cardsByRank.keySet.toString()}")
    cardsByRank.maxBy(_._2)._1
  }

}