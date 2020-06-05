package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.{Card, Deck}

import scala.collection.mutable.ListBuffer

case class PozzettoDeck private(override protected val cards: ListBuffer[Card]) extends Deck{

}


object PozzettoDeck{
  def apply(cards:List[Card]): PozzettoDeck = new PozzettoDeck(cards.to(ListBuffer))
}
