package com.abaddon83.burracoGame.writeModel.models.burracoGameendeds

import com.abaddon83.burracoGame.writeModel.models.burracos.Burraco
import com.abaddon83.burracoGame.writeModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.writeModel.models.decks.Card

data class BurracoPoint private constructor(
        override val identity: BurracoIdentity,
        override val cards: List<Card>
): Burraco() {
    constructor(burraco: Burraco): this(burraco.identity(),burraco.showCards())

}