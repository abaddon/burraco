package com.abaddon83.burraco.`match`.games.domainModels

import java.util.UUID

import com.abaddon83.burraco.shares.decks.Ranks.Two
import com.abaddon83.burraco.shares.decks.Suits.Suit
import com.abaddon83.burraco.shares.decks.{Card, Ranks}

import scala.collection.mutable.ListBuffer

case class ScaleId(id: UUID)

object ScaleId{
  def apply(): ScaleId = new ScaleId(UUID.randomUUID())
}


trait ScaleOrdered {

  private val scaleOrder = List(
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
    Ranks.Two
  )

  private def addJolly(jollyCards: ListBuffer[Card], twoCards:ListBuffer[Card], suit:Suit) ={
    if(!jollyCards.isEmpty){  //prendo un jolly o un 2 dando priorita' al jolly
      jollyCards.remove(0)
    }else if(!twoCards.isEmpty){
      twoCards.find(c => c.suit != suit) match {
        case Some(card) => twoCards.remove(twoCards.indexOf(card))
        case None => twoCards.remove(0)
      }
    }else
      throw new Exception("The scale sequence is broken")
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
    //moveAceOnHead(cards.sortWith(sortByRank))
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
            case None => throw new Exception("The scale sequence is broken")
          }
        }
        case _ => List().empty
      }
    }else {List().empty}

    if(!aceCards.isEmpty) {
      throw new Exception("The scale sequence is broken")
    }

    List(
      cardsToAppendOnHead,
      cardsSorted,
      cardsToAppendOnTail
    ).flatten


  }




  protected def sortScale(cards: List[Card], suit: Suit): List[Card]  ={
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
    List(appendAce(sortedList, aceCards, jollyCards, twoCards),jollyCards,twoCards).flatten
  }

}



case class Scale protected(
                  protected val scaleId: ScaleId,
                  protected val cards: List[Card],
                  protected val suit: Suit) extends ScaleOrdered{

  def showCards: List[Card]  = {
    cards
  }

  def getScaleId: ScaleId ={
    scaleId
  }

  def addCards(cardsToAdd: List[Card]): Scale = {
    val updatedCards = List(cards,cardsToAdd).flatten
    this.copy(cards =updatedCards).sort()

  }

  def sort(): Scale ={
    validateScale()
    val cardsSorted=this.sortScale(cards,suit)
    this.copy(cards = cardsSorted)
  }

  private def validateScale() ={
    assert(cards.size >=3, "A Scale is composed by 3 or more cards")
    val jollyCards = cards.filter(c => c.rank == Ranks.Jolly)
    val twoCards = cards.filter(c => c.rank == Two)
    val cardsWithoutJollies = (cards diff jollyCards) diff twoCards

    assert(jollyCards.size <=1,"A scale can have at least 1 Jolly")
    assert(twoCards.size <=2,"A scale can have at least 1 Jolly")

    if(cardsWithoutJollies.exists(c => c.suit != suit)){
      throw new IllegalArgumentException("A Scale is composed by cards with the same suit")
    }

  }

}

object Scale {


  def apply(cards: List[Card]): Scale = {
    Scale(ScaleId(),cards,scaleSuit(cards)).sort
  }

  private def scaleSuit(cards: List[Card]):Suit ={
    val list=cards.filter(c => c.rank != Ranks.Jolly).filter(c =>c.rank != Ranks.Two)
    list.head.suit
  }

}

