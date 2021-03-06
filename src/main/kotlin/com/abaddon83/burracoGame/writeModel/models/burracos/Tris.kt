package com.abaddon83.burracoGame.writeModel.models.burracos

import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks

abstract class Tris(className:String) :Burraco(className){
    protected abstract val rank: Ranks.Rank

    fun showRank(): Ranks.Rank = rank
    fun numCards():Int = cards.size


    abstract fun addCards(cardsToAdd: List<Card>): Tris
    protected abstract fun validateNewCards(cardsToValidate: List<Card>): List<Card>
}