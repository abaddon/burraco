package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses

import com.abaddon83.burracoGame.domainModels.BurracoPlayer

data class BurracoPlayerResponse(
        val id: PlayerIdentityResponse
) {
    constructor(player: BurracoPlayer): this(
        id = PlayerIdentityResponse(player.identity())
    )
}