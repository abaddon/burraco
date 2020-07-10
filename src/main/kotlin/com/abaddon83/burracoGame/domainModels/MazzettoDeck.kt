package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.decks.Deck

data class MazzettoDeck private constructor(override val cards: MutableList<Card>) : Deck {

    override fun grabFirstCard(): Card = throw UnsupportedOperationException("This method is not implemented in the Mazzetto")

    override fun grabAllCards(): List<Card> = throw UnsupportedOperationException("This method is not implemented in the Mazzetto")

    fun getCardList(): List<Card> = cards.toList()

    companion object Factory {
        fun create(cards:List<Card>): MazzettoDeck {
            require(cards.size == 11 || cards.size == 18){"Pozzetto Size wrong, current size: ${cards.size}"}
            return MazzettoDeck(cards.toMutableList())
        }

    }

}