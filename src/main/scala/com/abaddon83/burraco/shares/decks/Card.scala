package com.abaddon83.burraco.shares.decks

import java.awt.Color

import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.Suits.Suit


case class Card(suit: Suit, rank: Rank) extends Ordered[Card]  {

  override def toString: String = {
    s"card: ${suit.icon} - ${rank.label}"
  }

  override def compare(that: Card): Int = {
    //   return 0 if two cards are equal
    //   return 1 if this card is greater than passed one
    //   return -1 otherwise
    if(rank.position > that.rank.position){
      return -1
    }else if(rank.position == that.rank.position){
      return 0
    }else{
      return 1
    }
  }
}


object Ranks {

  val noFiguresRanks: Seq[Rank] = Seq(Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten)
  val fullRanks: Seq[Rank] = noFiguresRanks ++ Seq(Jack,Queen,King)

  sealed trait Rank {
    val label: String
    val position: Int
  }
  case object Ace extends Rank {
    override val label: String = "A"
    override val position: Int = 1
  }
  case object Two extends Rank {
    override val label: String = "2"
    override val position: Int = 2
  }
  case object Three extends Rank {
    override val label: String = "3"
    override val position: Int = 3
  }
  case object Four extends Rank {
    override val label: String = "4"
    override val position: Int = 4
  }
  case object Five extends Rank {
    override val label: String = "5"
    override val position: Int = 5
  }
  case object Six extends Rank {
    override val label: String = "6"
    override val position: Int = 6
  }
  case object Seven extends Rank {
    override val label: String = "7"
    override val position: Int = 7
  }
  case object Eight extends Rank {
    override val label: String = "8"
    override val position: Int = 8
  }
  case object Nine extends Rank {
    override val label: String = "9"
    override val position: Int = 9
  }
  case object Ten extends Rank {
    override val label: String = "10"
    override val position: Int = 10
  }
  case object Jack extends Rank {
    override val label: String = "J"
    override val position: Int = 11
  }
  case object Queen extends Rank {
    override val label: String = "Q"
    override val position: Int = 12
  }
  case object King extends Rank {
    override val label: String = "K"
    override val position: Int = 13
  }
  case object Jolly extends Rank {
    override val label: String = "Jolly"
    override val position: Int = 0
  }

}

object Suits {

  val allSuit = Seq(Heart,Tile,Clover,Pike)
  sealed trait Suit { val icon: Char; val color: Color}
  case object Heart extends Suit {
    override val icon: Char = '\u2764'
    override val color: Color = Color.RED
  }
  case object Tile extends Suit {
    override val icon: Char = '\u2666'
    override val color: Color = Color.RED
  }
  case object Clover extends Suit {
    override val icon: Char = '\u2663'
    override val color: Color = Color.BLACK
  }
  case object Pike extends Suit {
    override val icon: Char = '\u2660'
    override val color: Color = Color.BLACK
  }
  case object Jolly extends Suit {
    override val icon: Char = '\u2660'
    override val color: Color = Color.BLACK
  }
}





