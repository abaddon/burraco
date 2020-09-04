package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages

import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity

data class GameIdentityModule(val code: String, val type: String) {

    constructor(gameIdentity: GameIdentity) : this(
            code = gameIdentity.convertTo().toString(),
            type = gameIdentity.convertTo().javaClass.simpleName
    )
}