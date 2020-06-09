package com.abaddon83.burraco.`match`.games.domainModels

import java.util.UUID

import com.abaddon83.burraco.shares.decks.{Card, Ranks}
import com.abaddon83.burraco.shares.decks.Ranks.Two
import com.abaddon83.burraco.shares.decks.Suits.{Jolly, Suit}

import scala.collection.mutable.ListBuffer

case class ScaleId(id: UUID)

object ScaleId{
  def apply(): ScaleId = new ScaleId(UUID.randomUUID())
}

case class Scale private(scaleId: ScaleId, private val suit: Suit, private val cards: List[Card]) {

  def showCards: List[Card]  = {
    cards
  }

  def addCards(cardsToAdd: List[Card]): Scale = {
    val updatedCards = List(cards,cardsToAdd).flatten
    Scale.validateScale(updatedCards)
    copy(cards = updatedCards)
  }

}

object Scale {
  val scaleOrder = List( //13
    //Ranks.Ace,
    Ranks.King,
    Ranks.Queen,
    Ranks.Jack,
    Ranks.Ten,
    Ranks.Nine,  //4
    Ranks.Eight,
    Ranks.Seven,
    Ranks.Six,
    Ranks.Five,
    Ranks.Four,
    Ranks.Three,
    Ranks.Two,
    Ranks.Ace
  )
  def apply(cards: List[Card]): Scale = {
    validateScale(cards)
    val suit = cards.filterNot(c => c.suit == Jolly).head.suit
    sortScale(cards,suit)

    new Scale(ScaleId(),suit,cards)
  }

  def sortScale(cards: List[Card], scaleSuit: Suit): List[Card] ={
    val jollyCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Jolly))
    val twoCards = ListBuffer.from(cards.filter(c => c.rank == Two))
    val cardsWithoutJolly = (cards diff jollyCards) diff twoCards

    println("sortScale START ")
    println("jollyCards")
    jollyCards.foreach(c => println(c))
    println("twoCards")
    twoCards.foreach(c => println(c))
    println("cardsWithoutJolly")
    cardsWithoutJolly.foreach(c => println(c))
    println("sortScale END ")

    val sortedCardsWithoutJolly = cardsWithoutJolly.sortWith(sortByRank)
    sortedCardsWithoutJolly.foreach(println(_))
    //check if sequence has holes..
    val rightScaleOrder = getTheScaleOrderSubset(sortedCardsWithoutJolly)
    var sortedList = rightScaleOrder.map( rank =>
      sortedCardsWithoutJolly.find(card => card.rank == rank) match {
        case Some(card) => card
        case None => { //controllo tra i jolly
          rank match {
            case Ranks.Two => {
              twoCards.find(card => card.rank == rank && card.suit  == scaleSuit) match {
                case Some(card) => twoCards.remove(twoCards.indexOf(card)) //2 appartenente alla scala non e' un jolly
                case None => {
                  if(jollyCards.isEmpty){  //prendo un jolly o un 2 dando priorita' al jolly
                    jollyCards.remove(0)
                  }else if(!twoCards.isEmpty){
                    twoCards.find(c => c.suit != scaleSuit) match {
                      case Some(card) => twoCards.remove(twoCards.indexOf(card))
                      case None => twoCards.remove(0)
                    }
                  }else
                    throw new Exception("The scale sequence is broken")
                }
              }
            }
            case _ => {
              println(rank)
              sortedCardsWithoutJolly.find(card => card.rank == rank) match {
                case Some(card) => card
                case None => {
                  if(!jollyCards.isEmpty){  //prendo un jolly o un 2 dando priorita' al jolly
                    jollyCards.remove(0)
                  }else if(!twoCards.isEmpty){
                    twoCards.find(c => c.suit != scaleSuit) match {
                      case Some(card) => twoCards.remove(twoCards.indexOf(card))
                      case None => twoCards.remove(0)
                    }

                  }else
                    throw new Exception("The scale sequence is broken")
                }
              }

            }
          }
        }
      }
    )

    List(sortedList, jollyCards.toList, twoCards.toList).flatten
  }




  def getTheScaleOrderSubset(cards: List[Card]) ={
    val idxHead=findTheCardPosition(cards.head)
    val idxLast = scaleOrder.size - (findTheCardPosition(cards.last)+1)    //13 - 5
    scaleOrder.drop(idxHead).dropRight(idxLast)
    //println("getTheScaleOrderSubset START")
    //println(s"idxHead: ${idxHead} idxLast: ${idxLast}")
    //scaleOrderSubset.foreach(r => println(r))
    //println("getTheScaleOrderSubset END")
    //scaleOrderSubset
  }

  def findTheCardPosition(card: Card): Int ={
    scaleOrder.indexOf(card.rank)
  }


  def sortByRank(card: Card, cardNext: Card ) = {

    val idx = findTheCardPosition(card)
    scaleOrder.drop(idx+1).contains(cardNext.rank)
    //cardNext.rank == scaleOrder(idx + 1)
  }

    //val nextCard = cards(index)
    //)

    //val index=scaleOrderAccepted.indexOf(card.rank)
    //val nextIndex = Math.min(index+1,scaleOrderAccepted.size)

  private def validateScale(cards: List[Card]): Unit ={
    assert(cards.size >=3, "A Scale is composed by 3 or more cards")
    val jollyCards = cards.filter(c => c.rank == Ranks.Jolly)
    val twoCards = cards.filter(c => c.rank == Two)
    val cardsWithoutJollies = (cards diff jollyCards) diff twoCards

    assert(jollyCards.size <=1,"A scale can have at least 1 Jolly")
    assert(twoCards.size <=2,"A scale can have at least 1 Jolly")
    if(cardsWithoutJollies.exists(_.suit != cards.head.suit)){
      throw new IllegalArgumentException("A Scale is composed by cards with the same suit")
    }
    //TODO add the check regarding the card sequence

  }
}

