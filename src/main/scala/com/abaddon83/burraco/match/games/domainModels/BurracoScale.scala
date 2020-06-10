package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.{Card, Ranks}
import com.abaddon83.burraco.shares.decks.Suits.Suit

import scala.collection.mutable.ListBuffer

case class BurracoScale protected(
                                   override protected val scaleId: ScaleId,
                                   override protected val cards: List[Card],
                                   override protected val suit: Suit
                                 ) extends Scale{

  override def addCards(cardsToAdd: List[Card]): BurracoScale = {
    val updatedCards = validateScale(List(cards,cardsToAdd).flatten)
    this.copy(cards =updatedCards).sort()
  }


  override def sort(): BurracoScale  ={
    val aceCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Ace))
    val jollyCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Jolly))
    val twoCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Two))
    val cardsWithoutJolly = (cards diff jollyCards) diff twoCards
    val sortedCardsWithoutJollyAndAce = cardsWithoutJolly diff aceCards

    val sortedCardsWithoutJolly = sortCards(sortedCardsWithoutJollyAndAce)

    val rightScaleOrder = getTheScaleOrderSubset(sortedCardsWithoutJolly)

    val sortedList = rightScaleOrder.map( rank =>
      sortedCardsWithoutJolly.find(card => card.rank == rank) match {
        case Some(card) => card
        case None => { //controllo tra i jolly
          rank match {
            case Ranks.Two => {
              twoCards.find(card => card.rank == rank && card.suit  == suit) match {
                case Some(card) => twoCards.remove(twoCards.indexOf(card)) //2 appartenente alla scala non e' un jolly
                case None => addJolly(jollyCards,twoCards,suit)
              }
            }
            case _ => {
              sortedCardsWithoutJolly.find(card => card.rank == rank) match {
                case Some(card) => card
                case None => addJolly(jollyCards,twoCards,suit)
              }
            }
          }
        }
      }
    )
    this.copy(cards = List(appendAce(sortedList, aceCards, jollyCards, twoCards),jollyCards,twoCards).flatten)
  }

  private val scaleOrder = List(
    Ranks.Ace,
    Ranks.King,
    Ranks.Queen,
    Ranks.Jack,
    Ranks.Ten,
    Ranks.Nine,
    Ranks.Eight,
    Ranks.Seven,
    Ranks.Six,
    Ranks.Five,
    Ranks.Four,
    Ranks.Three,
    Ranks.Two,
    Ranks.Ace
  ).filter(r => r != Ranks.Ace)

  private def addJolly(jollyCards: ListBuffer[Card], twoCards:ListBuffer[Card], suit:Suit) ={
    if(!jollyCards.isEmpty){  //prendo un jolly o un 2 dando priorita' al jolly
      jollyCards.remove(0)
    }else if(!twoCards.isEmpty){
      twoCards.find(c => c.suit != suit) match {
        case Some(card) => twoCards.remove(twoCards.indexOf(card))
        case None => twoCards.remove(0)
      }
    }else
      throw new IllegalArgumentException("The scale sequence is broken")
  }

  private def getTheScaleOrderSubset(cards: List[Card]) ={
    val idxHead=findTheCardPosition(cards.head)
    val idxLast = scaleOrder.size - (findTheCardPosition(cards.last)+1)    //13 - 5
    scaleOrder.drop(idxHead).dropRight(idxLast)
  }

  private def findTheCardPosition(card: Card): Int ={
    scaleOrder.indexOf(card.rank)
  }

  private def sortByRank(card: Card, cardNext: Card ) = {
    val idx = findTheCardPosition(card)
    scaleOrder.drop(idx+1).contains(cardNext.rank)
  }

  private def sortCards(cards: List[Card]): List[Card] ={
    cards.sortWith(sortByRank)
  }

  private def appendAce(cardsSorted: List[Card], aceCards: ListBuffer[Card],jollyCards: ListBuffer[Card], twoCards: ListBuffer[Card]): List[Card] ={
    if(aceCards.isEmpty){
      return cardsSorted
    }
    val cardHead = cardsSorted.head
    val cardLast = cardsSorted.last

    val cardsToAppendOnHead = if(!aceCards.isEmpty){
      cardHead.rank match {
        case Ranks.King => List(aceCards.remove(0))
        case Ranks.Queen => {
          if(!jollyCards.isEmpty){
            List(aceCards.remove(0),jollyCards.remove(0))
          } else if(!twoCards.isEmpty){
            List(aceCards.remove(0),twoCards.remove(0))
          }else{
            List().empty
          }
        }
        case _ => List().empty
      }
    } else {List().empty}

    val cardsToAppendOnTail = if(!aceCards.isEmpty){
      cardLast.rank match {
        case Ranks.Two => List(aceCards.remove(0)) //List(List(aceCards.remove(0)), cardsSorted).flatten
        case Ranks.Three => {
          if(!jollyCards.isEmpty){
            List(jollyCards.remove(0), aceCards.remove(0))
          } else if(!twoCards.isEmpty){
            List(twoCards.remove(0), aceCards.remove(0))
          }else{
            List().empty
          }
        }
        case Ranks.Four => {
          twoCards.find(c => c.suit == cardsSorted.head.suit) match {
            case Some(cardTwoWithSameSuit) => {
              twoCards -= cardTwoWithSameSuit
              if(!jollyCards.isEmpty){
                List(jollyCards.remove(0), cardTwoWithSameSuit, aceCards.remove(0))
              } else if(!twoCards.isEmpty){
                List(twoCards.remove(0), cardTwoWithSameSuit, aceCards.remove(0))
              }else{
                List().empty
              }
            }
            case None => throw new IllegalArgumentException("The scale sequence is broken")
          }
        }
        case _ => List().empty
      }
    }else {List().empty}

    if(!aceCards.isEmpty) {
      throw new IllegalArgumentException("The scale sequence is broken")
    }

    List(
      cardsToAppendOnHead,
      cardsSorted,
      cardsToAppendOnTail
    ).flatten


  }
}


object BurracoScale {

  def apply(cards: List[Card]): BurracoScale = {
    assert(cards.size >=3, "A Scale is composed by 3 or more cards")
    BurracoScale(ScaleId(),cards,scaleSuit(cards)).sort
  }

  private def scaleSuit(cards: List[Card]):Suit ={
    val list=cards.filter(c => c.rank != Ranks.Jolly).filter(c =>c.rank != Ranks.Two)
    list.head.suit
  }

}
