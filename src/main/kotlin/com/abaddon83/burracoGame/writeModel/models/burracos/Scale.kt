package com.abaddon83.burracoGame.writeModel.models.burracos

import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits

abstract class Scale(className:String): Burraco(className) {

    fun numCards():Int = cards.size
    fun showSuit(): Suits.Suit = suit

    abstract fun addCards(cardsToAdd: List<Card>): Scale
    protected abstract val suit: Suits.Suit
    protected abstract fun validateNewCards(cardsToValidate: List<Card>): List<Card>

}