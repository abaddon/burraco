package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos

import com.abaddon83.cardsGames.shares.decks.{Card, Deck}

import scala.collection.mutable.ListBuffer

case class MazzettoDeck private(override protected val cards: ListBuffer[Card]) extends Deck{

  override def grabFirstCard(): Card = {
    throw new UnsupportedOperationException("This method is not implemented in the Mazzetto")
  }

  override def grabAllCards(): List[Card] = {
    throw new UnsupportedOperationException("This method is not implemented in the Mazzetto")
  }

  def getCards(): List[Card] = {
    cards.toList
  }


}


object MazzettoDeck{
  def build(cards:List[Card]): MazzettoDeck = {
    assert(cards.size == 11 || cards.size == 18, "Pozzetto Size wrong")
    new MazzettoDeck(ListBuffer.from(cards))
  }
}
