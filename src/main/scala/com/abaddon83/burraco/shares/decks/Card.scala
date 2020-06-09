package com.abaddon83.burraco.shares.decks

import java.awt.Color

import com.abaddon83.burraco.shares.decks.Ranks.Rank
import com.abaddon83.burraco.shares.decks.Suits.Suit


case class Card (suit: Suit, rank: Rank) {

  override def toString: String = {
    s"card: ${suit.icon} - ${rank.label}"
  }

}


object Ranks {

  val noFiguresRanks: Seq[Rank] = Seq(Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten)
  val fullRanks: Seq[Rank] = Seq(noFiguresRanks,Seq(Jack,Queen,King)).flatten

  sealed trait Rank {
    val label: String
  }
  case object Ace extends Rank {
    override val label: String = "A"
  }
  case object Two extends Rank {
    override val label: String = "2"
  }
  case object Three extends Rank {
    override val label: String = "3"
  }
  case object Four extends Rank {
    override val label: String = "4"
  }
  case object Five extends Rank {
    override val label: String = "5"
  }
  case object Six extends Rank {
    override val label: String = "6"
  }
  case object Seven extends Rank {
    override val label: String = "7"
  }
  case object Eight extends Rank {
    override val label: String = "8"
  }
  case object Nine extends Rank {
    override val label: String = "9"
  }
  case object Ten extends Rank {
    override val label: String = "10"
  }
  case object Jack extends Rank {
    override val label: String = "J"
  }
  case object Queen extends Rank {
    override val label: String = "Q"
  }
  case object King extends Rank {
    override val label: String = "K"
  }
  case object Jolly extends Rank {
    override val label: String = "Jolly"
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





