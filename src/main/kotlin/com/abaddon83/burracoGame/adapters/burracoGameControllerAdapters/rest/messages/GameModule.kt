package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.GameIdentityModule
import com.abaddon83.burracoGame.domainModels.BurracoGame

data class GameModule(
        val identity: GameIdentityModule,
        val type: String
) {
    constructor(game: BurracoGame): this(
            identity = GameIdentityModule(game.identity()),
            type = "Burraco")
}