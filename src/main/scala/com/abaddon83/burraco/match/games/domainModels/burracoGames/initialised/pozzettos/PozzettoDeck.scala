package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos

import com.abaddon83.burraco.shares.decks.{Card, Deck}

import scala.collection.mutable.ListBuffer

case class PozzettoDeck private(override protected val cards: ListBuffer[Card]) extends Deck{

}


object PozzettoDeck{
  def build(cards:List[Card]): PozzettoDeck = {
    assert(cards.size == 11 || cards.size == 18, "Pozzetto Size wrong")
    new PozzettoDeck(ListBuffer.from(cards))
  }
}
