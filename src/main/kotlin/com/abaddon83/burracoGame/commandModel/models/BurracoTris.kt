package com.abaddon83.burracoGame.commandModel.models

import com.abaddon83.burracoGame.commandModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.commandModel.models.burracos.Tris
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.Ranks

data class BurracoTris constructor(
        override val identity: BurracoIdentity,
        override val rank: Ranks.Rank,
        override val cards: List<Card>
): Tris() {

    override fun addCards(cardsToAdd: List<Card>): BurracoTris {
        return copy(cards = validateNewCards(cardsToAdd))
    }

    override fun validateNewCards(cardsToValidate: List<Card>): List<Card> {
        val tmpCardList=cardsToValidate.plus(this.cards)
        val cardsWithoutJolly = tmpCardList.filterNot {c -> c.rank == Ranks.Jolly || c.rank == Ranks.Two}
        check((tmpCardList.minus(cardsWithoutJolly)).size <= 1) { warnMsg("A tris can contain at least 1 Jolly or Two") }
        check(cardsWithoutJolly.filterNot { c -> c.rank == rank }.isEmpty()) { warnMsg("A tris is composed by cards with the same rank") }
        return tmpCardList
    }

    companion object Factory {
        fun create(cards: List<Card>): BurracoTris {
            check(cards.size >=3) { "A tris is composed by 3 or more cards" }
            val rank = calculateTrisRank(cards)
            check(!listOf(Ranks.Two,Ranks.Jolly).contains(rank)) { "Tris of Jolly or Two are not allowed" }
            return BurracoTris(identity = BurracoIdentity.create(), rank = rank, cards = cards)
        }

        private fun calculateTrisRank(cards: List<Card>): Ranks.Rank {
            val cardsByRank = cards.groupBy { c-> c.rank }.mapValues { (k,v) -> v.size }

            val cardsByRankWithoutJollyAndTwo = cardsByRank.minus(Ranks.Jolly).minus(Ranks.Two)

            assert(cardsByRankWithoutJollyAndTwo.keys.size ==1) {"Too many different ranks found: ${cardsByRank.keys}"}
            return checkNotNull(cardsByRank.maxBy { it.value }?.key){"Tris Rank calculation failed"}
        }

    }

}



/*
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
 */