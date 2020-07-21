package com.abaddon83.burracoGame.controller.adapter.messages

import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity

data class PlayerIdentityModule(val code: String, val type: String) {


    companion object Factory {
        fun from(playerIdentity: PlayerIdentity): PlayerIdentityModule =
                PlayerIdentityModule(
                        code = playerIdentity.convertTo().toString(),
                        type = playerIdentity.convertTo().javaClass.simpleName
                )
    }
}