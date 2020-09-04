package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages

import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits

data class CardsModule(
        val cards: List<CardModule>
) {

    fun to(): List<Card> =
            cards.map {
                Card(suit = Suits.valueOf(it.suit.name), rank = Ranks.valueOf(it.rank.name))
            }

    companion object Factory {
        fun from(cards: List<Card>): CardsModule =
                CardsModule(
                        cards.map { CardModule.from(it) }
                )
    }
}