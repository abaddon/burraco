package com.abaddon83.burraco.`match`.games.domainModels

import java.util.UUID

import com.abaddon83.burraco.shares.decks.Ranks.Two
import com.abaddon83.burraco.shares.decks.Suits.Suit
import com.abaddon83.burraco.shares.decks.{Card, Ranks}

case class ScaleId(id: UUID)

object ScaleId{
  def apply(): ScaleId = new ScaleId(UUID.randomUUID())
}

trait Scale {
  protected val scaleId: ScaleId
  protected val cards: List[Card]
  protected val suit: Suit

  def sort(): Scale

  def addCards(cardsToAdd: List[Card]): Scale

  def showCards: List[Card]  = {
    cards
  }

  def getScaleId: ScaleId ={
    scaleId
  }

  protected def validateScale(cards: List[Card]) ={
    assert(cards.size >=3, "A Scale is composed by 3 or more cards")
    val jollyCards = cards.filter(c => c.rank == Ranks.Jolly)
    val twoCards = cards.filter(c => c.rank == Two)
    val cardsWithoutJollies = (cards diff jollyCards) diff twoCards

    assert(jollyCards.size <=1,"A scale can have at least 1 Jolly")
    assert(twoCards.size <=2,"A scale can have at least 1 Jolly")

    if(cardsWithoutJollies.exists(c => c.suit != suit)){
      throw new IllegalArgumentException("A Scale is composed by cards with the same suit")
    }
    cards
  }

}



