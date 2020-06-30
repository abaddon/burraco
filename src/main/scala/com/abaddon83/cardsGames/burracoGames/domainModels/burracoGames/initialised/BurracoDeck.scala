package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.shares.decks.Ranks.Jolly
import com.abaddon83.cardsGames.shares.decks.{Card, Deck, Ranks, Suits}

import scala.collection.mutable.ListBuffer

case class BurracoDeck(override protected val cards: ListBuffer[Card]) extends Deck {

  def shuffle(): BurracoDeck ={
    BurracoDeck(scala.util.Random.shuffle(cards))
  }

  override def grabAllCards(): List[Card] = {
    throw new UnsupportedOperationException("You cannot grab all cards from the Deck")
  }

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