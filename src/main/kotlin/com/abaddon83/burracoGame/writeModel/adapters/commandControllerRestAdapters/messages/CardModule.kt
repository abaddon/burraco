package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages

import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits


data class CardModule(
        val suit: Suit,
        val rank: Rank
) {

    fun to(): Card =
            Card(suit = Suits.valueOf(suit.name), rank = Ranks.valueOf(rank.name))


    companion object Factory {
        fun from(card: Card): CardModule =
                CardModule(
                        suit = Suit.valueOf(card.suit.javaClass.simpleName),
                        rank = Rank.valueOf(card.rank.javaClass.simpleName)
                )
    }
}

enum class Rank { Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Jolly }
enum class Suit { Heart, Tile, Clover, Pike, Jolly }