package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages

import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity

data class PlayerIdentityModule(val code: String, val type: String) {


    companion object Factory {
        fun from(playerIdentity: PlayerIdentity): PlayerIdentityModule =
                PlayerIdentityModule(
                        code = playerIdentity.convertTo().toString(),
                        type = playerIdentity.convertTo().javaClass.simpleName
                )
    }
}