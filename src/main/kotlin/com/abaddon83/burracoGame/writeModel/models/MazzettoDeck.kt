package com.abaddon83.burracoGame.writeModel.models

import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Deck
import com.abaddon83.utils.logs.WithLog

data class MazzettoDeck private constructor(override val cards: MutableList<Card>) : Deck, WithLog() {

    override fun grabFirstCard(): Card = throw UnsupportedOperationException("This method is not implemented in the Mazzetto")

    override fun grabAllCards(): List<Card> = throw UnsupportedOperationException("This method is not implemented in the Mazzetto")

    fun getCardList(): List<Card> = cards.toList()

    companion object Factory {
        fun create(cards:List<Card>): MazzettoDeck {
            require(cards.size == 11 || cards.size == 18){"Mazzetto Size is wrong, current size: ${cards.size}"}
            return MazzettoDeck(cards.toMutableList())
        }

    }

}