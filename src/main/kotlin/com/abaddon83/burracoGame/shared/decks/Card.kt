package com.abaddon83.burracoGame.shared.decks

import java.awt.Color

data class Card(val suit: Suits.Suit, val rank: Ranks.Rank) : Comparable<Card> {

    override fun toString(): String {

        return "card: ${suit.icon} - ${rank.label}"
    }

    override fun compareTo(other: Card): Int {
        //   return 0 if two cards are equal
        //   return 1 if this card is greater than passed one
        //   return -1 otherwise
        if(rank.position > other.rank.position){
            return -1
        }else if(rank.position == other.rank.position){
            return 0
        }else{
            return 1
        }
    }

}

object Ranks {
    val noFiguresRanks: List<Rank> = listOf(Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten)
    val fullRanks: List<Rank> = listOf(noFiguresRanks, listOf(Jack, Queen, King)).flatten()

    interface Rank {
        val label: String
        val position: Int
    }

    object Ace : Rank {
        override val label: String = "A"
        override val position: Int = 1
    }

    object Two : Rank {
        override val label: String = "2"
        override val position: Int = 2
    }

    object Three : Rank {
        override val label: String = "3"
        override val position: Int = 3
    }

    object Four : Rank {
        override val label: String = "4"
        override val position: Int = 4
    }

    object Five : Rank {
        override val label: String = "5"
        override val position: Int = 5
    }

    object Six : Rank {
        override val label: String = "6"
        override val position: Int = 6
    }

    object Seven : Rank {
        override val label: String = "7"
        override val position: Int = 7
    }

    object Eight : Rank {
        override val label: String = "8"
        override val position: Int = 8
    }

    object Nine : Rank {
        override val label: String = "9"
        override val position: Int = 9
    }

    object Ten : Rank {
        override val label: String = "10"
        override val position: Int = 10
    }

    object Jack : Rank {
        override val label: String = "J"
        override val position: Int = 11
    }

    object Queen : Rank {
        override val label: String = "Q"
        override val position: Int = 12
    }

    object King : Rank {
        override val label: String = "K"
        override val position: Int = 13
    }

    object Jolly : Rank {
        override val label: String = "Jolly"
        override val position: Int = 0
    }
}

object Suits {
    val allSuit: List<Suit> = listOf(Heart, Tile, Clover, Pike)

    interface Suit {
        val icon: Char;
        val color: Color
    }

    object Heart : Suit {
        override val icon: Char = '\u2764'
        override val color: Color = Color.RED
    }

    object Tile : Suit {
        override val icon: Char = '\u2666'
        override val color: Color = Color.RED
    }

    object Clover : Suit {
        override val icon: Char = '\u2663'
        override val color: Color = Color.BLACK
    }

    object Pike : Suit {
        override val icon: Char = '\u2660'
        override val color: Color = Color.BLACK
    }

    object Jolly : Suit {
        override val icon: Char = '\u2660'
        override val color: Color = Color.BLACK
    }

}