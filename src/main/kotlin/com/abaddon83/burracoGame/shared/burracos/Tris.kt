package com.abaddon83.burracoGame.shared.burracos

import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.decks.Ranks

abstract class Tris :Burraco(){
    protected abstract val rank: Ranks.Rank

    fun showRank(): Ranks.Rank = rank
    fun numCards():Int = cards.size


    abstract fun addCards(cardsToAdd: List<Card>): Tris
    protected abstract fun validateNewCards(cardsToValidate: List<Card>): List<Card>
}