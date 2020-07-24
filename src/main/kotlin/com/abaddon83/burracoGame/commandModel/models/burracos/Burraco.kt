package com.abaddon83.burracoGame.commandModel.models.burracos

import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.utils.ddd.Entity


abstract class Burraco : Entity<BurracoIdentity>() {

    protected abstract val cards: List<Card>

    fun showCards(): List<Card> = cards

    fun isBurraco(): Boolean = cards.size >= 7

}