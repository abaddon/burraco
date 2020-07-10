package com.abaddon83.burracoGame.domainModels.burracoGameendeds

import com.abaddon83.burracoGame.shared.burracos.Burraco
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card

data class BurracoPoint private constructor(
        override val identity: BurracoIdentity,
        override val cards: List<Card>
): Burraco() {
    constructor(burraco: Burraco): this(burraco.identity(),burraco.showCards())

}