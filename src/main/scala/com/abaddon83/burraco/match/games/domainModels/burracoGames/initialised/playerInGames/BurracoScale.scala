package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{Scale, ScaleId}
import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.Suits.Suit
import com.abaddon83.burraco.shares.decks.{Card, Ranks}

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
    val aceCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Ace)) //Ace
    val jollyCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Jolly)) //jolly
    val twoCards = ListBuffer.from(cards.filter(c => c.rank == Ranks.Two)) //The card Two, if not used as Two is a jolly
    val cardsWithoutJolly = cards diff (jollyCards ++ twoCards)
    val cardsWithoutJollyAndAce = cardsWithoutJolly diff aceCards

    val sortedCardsWithoutJollyAndAce = cardsWithoutJollyAndAce.sortWith(sortByRank)

    val rankSequence = getRankSequence(sortedCardsWithoutJollyAndAce)

    // try to verify if the card list has a card for each rank element in the sequence
    val sortedList = rankSequence.map( rank =>
      sortedCardsWithoutJollyAndAce.find(card => card.rank == rank) match {
        case Some(card) => card  //card found, I use it
        case None => findAnAlternativeCard(rank,jollyCards,twoCards) //card not found, I try to use a Jolly or a Two as alternative
      }
    )

    val sortedListWithAce = appendAce(sortedList, aceCards, jollyCards, twoCards) //The sequence previously calculated did't include the Ace. Now I try to append them on the head of the sequence or at the end.

    val sortedListWithTwoAsNormalCard = appendTwoAsNormalCard(sortedListWithAce,twoCards,jollyCards)

    val sortedListWithAllCards = appendRemainingJolly(sortedListWithTwoAsNormalCard,jollyCards.toList ++ twoCards.toList)

    assert(sortedListWithAllCards.size == cards.size)

    this.copy(cards = sortedListWithAllCards)
  }

  private def findAnAlternativeCard(rank: Rank,jollyCards: ListBuffer[Card], twoCards:ListBuffer[Card] ) = {
    {
      rank match {
        case Ranks.Two => { //if the rank missing in the sequence is the two then I try to use a two with the same scale suit
          twoCards.find(card => card.rank == rank && card.suit  == suit) match {
            case Some(card) => twoCards.remove(twoCards.indexOf(card)) //this 2 is not a jolly in this position, we have to consider it as normal card
            case None => addJolly(jollyCards,twoCards,suit) //nothing found, I try to find an alternative to cover the position Two
          }
        }
        case _ => addJolly(jollyCards,twoCards,suit) //I try to find an alternative to cover any other position missing
      }
    }
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

  private def getRankSequence(cards: List[Card]) ={
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

  private def appendCardAceOnHead(cardHead: Card, aceCards: ListBuffer[Card],jollyCards: ListBuffer[Card], twoCards: ListBuffer[Card]): List[Card] = {

    if(!aceCards.isEmpty){
      cardHead.rank match {
        case Ranks.King => List(aceCards.remove(0))
        case Ranks.Queen => {
          if(!jollyCards.isEmpty){
            List(aceCards.remove(0),jollyCards.remove(0))
          } else if(!twoCards.isEmpty){
            List(aceCards.remove(0),twoCards.remove(0))
          }else{
            List()
          }
        }
        case _ => List()
      }
    } else List()
  }

  private def appendCardAceOnTail(cardLast: Card, aceCards: ListBuffer[Card],jollyCards: ListBuffer[Card], twoCards: ListBuffer[Card]): List[Card] = {

    if(!aceCards.isEmpty){
      cardLast.rank match {
        case Ranks.Two => List(aceCards.remove(0))
        case Ranks.Three => {
          if(!twoCards.isEmpty){
            List(twoCards.remove(0), aceCards.remove(0))
          } else if(!jollyCards.isEmpty){
            List(jollyCards.remove(0), aceCards.remove(0))
          }else{
            List()
          }
        }
        case Ranks.Four => {
          twoCards.find(c => c.suit == suit) match {
            case Some(cardTwoWithSameSuit) => {
              twoCards -= cardTwoWithSameSuit
              if(!jollyCards.isEmpty){
                List(jollyCards.remove(0), cardTwoWithSameSuit, aceCards.remove(0))
              } else if(!twoCards.isEmpty){
                List(twoCards.remove(0), cardTwoWithSameSuit, aceCards.remove(0))
              }else{
                List()
              }
            }
            case None => throw new IllegalArgumentException("The scale sequence is broken")
          }
        }
        case _ => List()
      }
    }else {List()}
  }

  private def appendAce(cardsSorted: List[Card], aceCards: ListBuffer[Card],jollyCards: ListBuffer[Card], twoCards: ListBuffer[Card]): List[Card] ={
    if(aceCards.isEmpty){
      return cardsSorted
    }

   val  cardsSortedWithAce = List(
     appendCardAceOnHead(cardsSorted.head,aceCards,jollyCards,twoCards),
      cardsSorted,
     appendCardAceOnTail(cardsSorted.last,aceCards,jollyCards,twoCards)
    ).flatten

    if(!aceCards.isEmpty) {
      throw new IllegalArgumentException("The scale sequence is broken")
    }

    cardsSortedWithAce
  }

  private def appendTwoAsNormalCard(cardsSorted: List[Card],twoCards: ListBuffer[Card],jollyCards: ListBuffer[Card]): List[Card] ={
    if(twoCards.isEmpty){
      return cardsSorted
    }

    val cardTwoList = if(!twoCards.isEmpty){
      cardsSorted.last.rank match {
        case Ranks.Three => List(twoCards.remove(0))
        case Ranks.Four => {
          if(!jollyCards.isEmpty){
            List(jollyCards.remove(0), twoCards.remove(0))
          }else{
            List()
          }
        }
        case _ => List()
      }
    }else List()

    cardsSorted ++ cardTwoList
  }

  private def appendRemainingJolly(cardsSorted: List[Card],jollyList: List[Card]): List[Card] ={

    if (jollyList.isEmpty){
      return cardsSorted
    }
    cardsSorted.head.rank match {
      case Ranks.Ace => {
        cardsSorted.last.rank match {
          case Ranks.Ace => throw new IllegalArgumentException("The scale is not valid")
          case _ => {
            val card = jollyList.last
            appendRemainingJolly(cardsSorted ++ List(card),jollyList diff List(card))
          }
        }
      }
      case _ => {
        val card = jollyList.last
        appendRemainingJolly(List(card) ++ cardsSorted,jollyList diff List(card))
      }
    }
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
