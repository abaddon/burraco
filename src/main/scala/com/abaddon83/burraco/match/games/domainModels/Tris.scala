package com.abaddon83.burraco.`match`.games.domainModels

import java.util.UUID

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.decks.Ranks.{Jolly, Rank, Two}

case class Tris private(trisId: TrisId, private val rank: Rank, private val cards: List[Card]) {

  def showCards: List[Card]  = {
    cards
  }

  def addCards(cardsToAdd: List[Card]): Tris = {
    val updatedCards = List(cards,cardsToAdd).flatten
    Tris.validateTris(updatedCards)
    copy(cards = updatedCards)
  }

}


case class TrisId(id: UUID)

object TrisId{
  def apply(): TrisId = new TrisId(UUID.randomUUID())
}

object Tris{
  def apply(cards: List[Card]): Tris = {
    validateTris(cards)
    val rank = cards.filterNot(c => c.rank == Jolly || c.rank == Two).head.rank
    new Tris(TrisId(),rank,cards)
  }

  private def validateTris(cards: List[Card]): Unit ={
    println("")
    println("START TRIS")
    cards.foreach(c => println(s"card: ${c.toString}"))
    println(" --- cardsWithoutJolly -- ")
    assert(cards.size >=3, "A tris is composed by 3 or more cards")
    val cardsWithoutJolly = cards.filterNot(c => c.rank == Jolly || c.rank == Two)
    cardsWithoutJolly.foreach(c => println(s"card: ${c.toString}"))
    println("END TRIS")

    assert((cards diff cardsWithoutJolly).size <= 1, "A tris can contain at least 1 Jolly or Two")
    if(cardsWithoutJolly.exists(_.rank != cards.head.rank)){
      throw new IllegalArgumentException("A tris is composed cards with the same rank")
    }

  }
}