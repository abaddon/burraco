package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Ranks.Jolly
import com.abaddon83.burraco.shares.decks.{Card, Deck, Ranks, Suits}

import scala.collection.mutable.ListBuffer

case class BurracoDeck(override val cards: ListBuffer[Card]) extends Deck {

}

object BurracoDeck {

  def apply(): BurracoDeck =  {
    val cards: List[Card] = List(
      Deck.allRanksWithJollyCards(),
      Deck.allRanksWithJollyCards()
    ).flatten

    assert(cards.size == 108)
    assert(cards.count(card => card.rank == Ranks.Jolly && card.suit == Suits.Jolly) == 4,s"The deck doesn't contain 4 ${Jolly.label}")
    Suits.allSuit.foreach( suit => assert(cards.count(card => card.suit == suit) == 26,s"The card list doesn't contain 26 ${suit.icon} cards"))
    Ranks.fullRanks.foreach( rank => assert(cards.count(card => card.rank == rank) == 8,s"The card list doesn't contain 8 ${rank.label} cards"))

    BurracoDeck(cards.to(ListBuffer))
  }
}
