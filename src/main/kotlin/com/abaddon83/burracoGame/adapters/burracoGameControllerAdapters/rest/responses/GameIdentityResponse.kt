package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses

import com.abaddon83.burracoGame.shared.games.GameIdentity

data class GameIdentityResponse(val code: String, val type: String) {

    constructor(gameIdentity: GameIdentity) : this(
            code = gameIdentity.convertTo().toString(),
            type = gameIdentity.convertTo().javaClass.simpleName
    )
}