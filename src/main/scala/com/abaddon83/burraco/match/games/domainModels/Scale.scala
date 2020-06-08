package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.decks.Ranks.Two
import com.abaddon83.burraco.shares.decks.Suits.{Jolly, Suit}

case class Scale private(private val suit: Suit, private val cards: List[Card]) {

  def showCards: List[Card]  = {
    cards
  }
}

object Scale {

  def apply(cards: List[Card]): Scale = {
    validateScale(cards)
    val suit = cards.filterNot(c => c.suit == Jolly).head.suit
    new Scale(suit,cards)
  }

  private def validateScale(cards: List[Card]): Unit ={
    assert(cards.size >=3, "A Scale is composed by 3 or more cards")
    val cardsWithoutJolly = cards.filterNot(c => c.rank == Jolly || c.rank == Two)     // TODO TO FIX it's wrong
    assert(cardsWithoutJolly.size <2, "A Scale can contain at least 1 Jolly or Two")   // TODO TO FIX it's wrong
    if(cardsWithoutJolly.exists(_.suit != cards.head.suit)){
      throw new IllegalArgumentException("A tris is composed cards with the same suit")
    }
    //TODO add the check regarding the card sequence

  }
}

