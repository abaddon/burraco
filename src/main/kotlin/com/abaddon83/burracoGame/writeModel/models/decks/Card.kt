package com.abaddon83.burracoGame.writeModel.models.decks

import com.abaddon83.burracoGame.writeModel.models.BurracoTris
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import java.lang.Exception

@Serializable( with = CardCustomSerializer::class)
data class Card(val suit: Suits.Suit, val rank: Ranks.Rank) : Comparable<Card> {

    companion object Factory{
        fun fromLabel(label: String): Card{
            val elements = label.split("-")
            return Card(Suits.valueOf(elements[0]),Ranks.valueOf(elements[1]))
        }
    }

    override fun toString(): String {

        return "card: ${suit.icon} - ${rank.label}"
    }

    val label: String = "${suit.label}-${rank.label}"

    override fun compareTo(other: Card): Int {
        //   return 0 if two cards are equal
        //   return 1 if this card is greater than passed one
        //   return -1 otherwise
        if (rank.position > other.rank.position) {
            return -1
        } else if (rank.position == other.rank.position) {
            return 0
        } else {
            return 1
        }
    }

}

object CardCustomSerializer : KSerializer<Card> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Card", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Card) {
        val cardString = Json.encodeToJsonElement(value.let { card ->
            mapOf( "rank" to card.rank.javaClass.simpleName, "suit" to card.suit.javaClass.simpleName)
        })
        encoder.encodeString(cardString.toString())
    }

    override fun deserialize(decoder: Decoder): Card {
        val cardJson = Json.decodeFromString<JsonElement>(decoder.decodeString())
        val cardMap = Json.decodeFromJsonElement<Map<String,String>>(cardJson)

        return Card(
                suit = Suits.valueOf(cardMap.getValue("suit")),
                rank = Ranks.valueOf(cardMap.getValue("rank"))
        )
    }
}

object Ranks {
    val noFiguresRanks: List<Rank> = listOf(Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten)
    val fullRanks: List<Rank> = listOf(noFiguresRanks, listOf(Jack, Queen, King)).flatten()

    fun valueOf(value: String): Ranks.Rank =
            when (val rank = fullRanks.plus(Jolly).find { it.javaClass.simpleName.toLowerCase() == value.toLowerCase() }) {
                is Rank -> rank
                else -> throw Exception("$value is not a valid Rank")
            }

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

    fun valueOf(value: String): Suit =
            when (val suit = Suits.allSuit.plus(Jolly).find { it.javaClass.simpleName.toLowerCase() == value.toLowerCase() }) {
                is Suit -> suit
                else -> throw Exception("$value is not a valid Suit")
            }

    enum class Color{RED,BLACK}

    interface Suit {
        val icon: Char;
        val color: Color
        val label: String
    }

    object Heart : Suit {
        override val icon: Char = '\u2764'
        override val color: Color = Color.RED
        override val label: String = javaClass.simpleName.toLowerCase()
    }

    object Tile : Suit {
        override val icon: Char = '\u2666'
        override val color: Color = Color.RED
        override val label: String = javaClass.simpleName.toLowerCase()
    }

    object Clover : Suit {
        override val icon: Char = '\u2663'
        override val color: Color = Color.BLACK
        override val label: String = javaClass.simpleName.toLowerCase()
    }

    object Pike : Suit {
        override val icon: Char = '\u2660'
        override val color: Color = Color.BLACK
        override val label: String = javaClass.simpleName.toLowerCase()
    }

    object Jolly : Suit {
        override val icon: Char = '\u2660'
        override val color: Color = Color.BLACK
        override val label: String = javaClass.simpleName.toLowerCase()
    }

}