package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages

import com.abaddon83.burracoGame.writeModel.models.BurracoGame

data class GameModule(
        val identity: GameIdentityModule,
        val type: String
) {
    constructor(game: BurracoGame): this(
            identity = GameIdentityModule(game.identity()),
            type = "Burraco")
}