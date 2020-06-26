package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Ranks.{Rank, Two}
import com.abaddon83.burraco.shares.decks.Suits.Suit
import com.abaddon83.burraco.shares.decks.{Card, Ranks}


trait Scale {
  protected val cards: List[Card]
  protected val suit: Suit
  protected val scaleOrder: List[Rank]

  def sort(): Scale

  def addCards(cardsToAdd: List[Card]): Scale

  protected def validateNewCards(cards: List[Card]): Scale

}



