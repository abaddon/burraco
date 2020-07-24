package com.abaddon83.burracoGame.api.messages

import com.abaddon83.burracoGame.commandModel.models.BurracoGame

data class GameModule(
        val identity: GameIdentityModule,
        val type: String
) {
    constructor(game: BurracoGame): this(
            identity = GameIdentityModule(game.identity()),
            type = "Burraco")
}