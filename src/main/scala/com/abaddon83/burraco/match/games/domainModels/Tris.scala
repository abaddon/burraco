package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.{Card, Ranks}




trait Tris {
  protected val rank: Rank
  protected val cards: List[Card]

  def showRank(): Rank = rank
  def showCards(): List[Card] = cards

  def addCards(cardsToAdd: List[Card]): Tris

  def numCards():Int = {
    cards.size
  }

  protected def validateNewCards(cards: List[Card]): Tris

}
