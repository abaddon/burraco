package com.abaddon83.burracoGame.commandModel.models.burracoGameendeds

import com.abaddon83.burracoGame.commandModel.models.burracos.Burraco
import com.abaddon83.burracoGame.commandModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.commandModel.models.decks.Card

data class BurracoPoint private constructor(
        override val identity: BurracoIdentity,
        override val cards: List<Card>
): Burraco() {
    constructor(burraco: Burraco): this(burraco.identity(),burraco.showCards())

}