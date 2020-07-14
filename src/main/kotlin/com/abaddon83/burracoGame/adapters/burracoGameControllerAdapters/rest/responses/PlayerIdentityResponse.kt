package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses

import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class PlayerIdentityResponse(val code: String, val type: String) {

    constructor(playerIdentity: PlayerIdentity) : this(
            code = playerIdentity.convertTo().toString(),
            type = playerIdentity.convertTo().javaClass.simpleName
    )
}