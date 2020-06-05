package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.{Card, Deck}

import scala.collection.mutable.ListBuffer

case class DiscardPile private(override protected val cards: ListBuffer[Card]) extends Deck{

  override def grabAllCards(): List[Card] ={
    assert(cards.size >0,"The DiscardPile is empty, you can't grab a card from here")
    super.grabAllCards()
  }

  override def grabFirstCard(): Card = {
    throw new UnsupportedOperationException("You cannot grab only one card from the DiscardPile")
  }

  def addCard(card: Card): DiscardPile = {
    cards.addOne(card)
    DiscardPile(cards)
  }
}

object DiscardPile{
  def apply(cards: List[Card]): DiscardPile = new DiscardPile(cards.to(ListBuffer))
}


