package com.abaddon83.cardsGames.burracoGames.domainModels

import com.abaddon83.cardsGames.shares.decks.Suits.Suit
import com.abaddon83.cardsGames.shares.decks.{Card, Ranks}


trait Scale {
  protected val cards: List[Card]
  protected val suit: Suit


  def addCards(cardsToAdd: List[Card]): Scale

  def numCards():Int = {
    cards.size
  }

  protected def validateNewCards(cards: List[Card]): Scale


}



