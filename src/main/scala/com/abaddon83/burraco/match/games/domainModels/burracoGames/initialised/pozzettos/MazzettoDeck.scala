package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.{Card, Deck}

import scala.collection.mutable.ListBuffer

case class MazzettoDeck private(override protected val cards: ListBuffer[Card]) extends Deck{

  override def grabFirstCard(): Card = {
    throw new UnsupportedOperationException("You cannot grab only one card from the DiscardPile")
  }


}


object MazzettoDeck{
  def build(cards:List[Card]): MazzettoDeck = {
    assert(cards.size == 11 || cards.size == 18, "Pozzetto Size wrong")
    new MazzettoDeck(ListBuffer.from(cards))
  }
}
