package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses

import com.abaddon83.burracoGame.domainModels.BurracoGame

data class GameResponse(
        val identity: GameIdentityResponse,
        val type: String
) {
    constructor(game: BurracoGame): this(
            identity = GameIdentityResponse(game.identity()),
            type = "Burraco")
}