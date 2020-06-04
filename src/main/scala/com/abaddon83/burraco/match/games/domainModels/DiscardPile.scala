package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card

import scala.collection.mutable.ListBuffer

case class DiscardPile(cards: ListBuffer[Card]){

  def grabAllCards(): List[Card] ={
    assert(cards.size >0,"The DiscardPile is empty, you can't grab a card from here")
    val grabbedCards = cards.toList
    cards.empty
    assert(cards.size == 0,"The DiscardPile has to be empty")
    grabbedCards
  }

  def discardCard(card: Card): DiscardPile = {
    cards.addOne(card)
    DiscardPile(cards)
  }
}

object DiscardPile{
  def apply(cards: List[Card]): DiscardPile = new DiscardPile(cards.to(ListBuffer))
}


