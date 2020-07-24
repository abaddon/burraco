package com.abaddon83.burracoGame.commandModel.models.burracos

import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.Suits

abstract class Scale: Burraco() {

    fun numCards():Int = cards.size

    abstract fun addCards(cardsToAdd: List<Card>): Scale
    protected abstract val suit: Suits.Suit
    protected abstract fun validateNewCards(cardsToValidate: List<Card>): List<Card>

}