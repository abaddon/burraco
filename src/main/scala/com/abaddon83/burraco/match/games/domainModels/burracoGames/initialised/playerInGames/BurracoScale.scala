package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId, Scale}
import com.abaddon83.burraco.shares.decks.Suits.Suit
import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}

import scala.collection.mutable.ListBuffer

case class BurracoScale protected(
                                   override protected val burracoId: BurracoId,
                                   override protected val cards: List[Card],
                                   override protected val suit: Suit
                                 ) extends Burraco with Scale {

  override def addCards(cardsToAdd: List[Card]): BurracoScale = {
    validateNewCards(cardsToAdd)
  }


  def isBurraco(): Boolean = {
    if (cards.size < 7) false
    else true
  }

  protected def validateNewCards(cardsToAdd: List[Card]) = {
    assert(cardsToAdd.exists(c => c.suit!= suit && c.suit!=Suits.Jolly))
    this.copy(
      cards = BurracoScale.validateSequence(cards ++ cardsToAdd,suit)
    )
  }
}


object BurracoScale {

  def apply(cards: List[Card]): BurracoScale = {
    assert(cards.size >= 3, "A Scale is composed by 3 or more cards")
    val scalaSuit = calculateScaleSuit(cards)
    val scalaCards = validateSequence(cards, scalaSuit)

    //println("outcome:")
    //scalaCards.foreach(c => println(s"${c.suit} ${c.rank}"))

    BurracoScale(BurracoId(), scalaCards, scalaSuit)
  }

  private def calculateScaleSuit(cards: List[Card]): Suit = {
    val cardsBySuit = cards.groupMapReduce(c => c.suit)(_ => 1)(_ + _)
    cardsBySuit.foreach(i => println(s"${i._1}, ${i._2}"))
    assert(cardsBySuit.keySet.size <= 2, s"Too many different suits found: ${cardsBySuit.keySet.toString()}")
    val primarySuit = cardsBySuit.maxBy(_._2)._1
    if (cardsBySuit.keySet.size == 2) {
      val numCardsSecondarySuit = cardsBySuit.minBy(_._2)._2
      assert(numCardsSecondarySuit == 1, s"Found ${numCardsSecondarySuit} with not a ${primarySuit} suit, max allowed is 1")
    }
    primarySuit
  }

  protected def validateSequence(cards: List[Card], suit: Suit): List[Card] = {
    val jollies = getJollies(cards, suit)
    val aces = cards.filter(c => c.rank == Ranks.Ace)
    val sorted = ((cards diff jollies) diff aces).sorted[Card]
    val firstSort = (0 until sorted.size).flatMap { idx =>
      val currentPositionValue = sorted(idx).rank.position
      val nextPositionValue = sorted(Math.min(idx + 1, sorted.size - 1)).rank.position
      if (currentPositionValue - 1 == nextPositionValue) {
        // nessun buco
        println(s"${currentPositionValue}-1 == ${nextPositionValue}")
        List(sorted(idx))
      } else if (currentPositionValue - 2 == nextPositionValue) {
        // un buco, aggiungo un jolly
        println(s"${currentPositionValue}-2 == ${nextPositionValue}")
        assert(jollies.size != 0, "Jolly missing")
        List(sorted(idx)) ++ jollies
      } else if (currentPositionValue == nextPositionValue) { //sono a fine lista
        println(s"${currentPositionValue} == ${nextPositionValue}")
        List(sorted(idx))
      } else {
        println(s"${currentPositionValue}-? == ${nextPositionValue}")
        assert(false, "The hole is too big..")
        List[Card]()
      }
    }.toList
    val sortedWithAce = appendAce(firstSort, aces, jollies diff firstSort)

    appendRemainingJolly(sortedWithAce, jollies diff sortedWithAce)

  }

  private def appendRemainingJolly(cards: List[Card], jollies: List[Card]): List[Card] = {
    if (jollies.size == 0) return cards
    if (cards.head.rank!=Ranks.Ace) jollies ++ cards
    else if (cards.last.rank != Ranks.Ace) cards ++ jollies
    else {assert(false,"there isn't a space to put a jolly in the Scale, the scalle is full"); List()}
  }

  private def appendAce(cards: List[Card], aces: List[Card], jollies: List[Card]): List[Card] = {
    if (aces.size == 0) return cards

    println(s"cards.head.rank: ${cards.head.rank}")
    println(s"cards.last.rank: ${cards.last.rank}")
    println(s"jollies.size: ${jollies.size}")
    if (cards.head.rank == Ranks.King)  aces ++ cards
    else if (cards.head.rank == Ranks.Queen && jollies.size == 1)  aces ++ jollies ++ cards
    else if (cards.head.rank == Ranks.Queen && jollies.size == 0) {
      assert(false, "Jolly missing to cover the hole"); List()
    }
    else if (cards.last.rank == Ranks.Two) return cards ++ aces
    else if (cards.last.rank == Ranks.Three && jollies.size == 1) return cards ++ jollies ++ aces
    else if (cards.last.rank == Ranks.Three && jollies.size == 0) {
      assert(false, "Jolly missing to cover the hole"); List()
    }
    else {
      assert(false, "boh"); List()
    }
  }

  private def getJollies(cards: List[Card], suit: Suit): List[Card] = {
    val tmpJollies = cards.filter(c => c.rank == Ranks.Jolly || c.rank == Ranks.Two)
    println(s"tmpJollies: ${tmpJollies}")
    val jollies = tmpJollies.size match {
      //se la dimensione e' 2 significa che una delle 2 e' sicuramente un 2 valido
      case 2 => tmpJollies.filterNot(c => c.suit == suit && c.rank == Ranks.Two)
      case _ => tmpJollies
    }
    assert(jollies.size <= 1, s"Too many jollies found: ${jollies}, max allowed 1")
    println(s"Jollies: ${jollies}")

    jollies
  }

}
