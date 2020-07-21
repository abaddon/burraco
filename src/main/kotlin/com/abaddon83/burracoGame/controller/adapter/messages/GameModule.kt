package com.abaddon83.burracoGame.controller.adapter.messages

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.controller.adapter.messages.GameIdentityModule

data class GameModule(
        val identity: GameIdentityModule,
        val type: String
) {
    constructor(game: BurracoGame): this(
            identity = GameIdentityModule(game.identity()),
            type = "Burraco")
}