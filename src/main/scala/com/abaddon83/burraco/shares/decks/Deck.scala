package com.abaddon83.burraco.shares.decks

import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.Suits.Suit

import scala.collection.mutable.ListBuffer

trait Deck{
  protected val cards: ListBuffer[Card]

  /*def addCard(card: Card): this = {
    cards.addOne(card)
    (cards)
  }*/

  def numCards(): Int = {
    cards.size
  }

  def grabFirstCard(): Card ={
    cards.remove(0)
  }

  def grabAllCards(): List[Card] ={
    val grabbedCards = cards.toList
    cards.remove(0,cards.size)

    assert(cards.size == 0)

    grabbedCards
  }

}


object Deck {

  def allRanksWithJollyCards(): List[Card] = {
    val cards = List(
      allRanksCards(),
      addJolly()
    ).flatten
    assert(cards.size == 54)
    cards
  }

  def allRanksCards(): List[Card] = {
    val cards = Suits.allSuit.map(suit => buildCardSuit(suit,Ranks.fullRanks)).toList.flatten
    assert(cards.size == 52, "The card list has to contain exactly 52 cards")
    Suits.allSuit.foreach( suit => assert(cards.count(card => card.suit == suit) == 13,s"The card list doesn't contain 13 ${suit.icon} cards"))
    Ranks.fullRanks.foreach( rank => assert(cards.count(card => card.rank == rank) == 4,s"The card list doesn't contain 4 ${rank.label} cards"))
    cards
  }

  def addJolly(): List[Card] ={
    val cards = List(Card(Suits.Jolly,Ranks.Jolly),Card(Suits.Jolly,Ranks.Jolly))
    assert(cards.size == 2, "The card list has to contain exactly 2 cards")
    assert(cards.count(card => card.rank == Ranks.Jolly && card.suit == Suits.Jolly) == 2,s"The card list doesn't contain 2 ${Ranks.Jolly.label} cards")
    cards
  }

  private def  buildCardSuit(suit: Suit, ranks: Seq[Rank]): List[Card] ={
    ranks.map(rank => Card(suit,rank)).toList
  }
}

