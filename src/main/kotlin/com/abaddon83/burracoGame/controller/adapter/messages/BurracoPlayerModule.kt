package com.abaddon83.burracoGame.controller.adapter.messages

import com.abaddon83.burracoGame.commandModel.models.BurracoPlayer

data class BurracoPlayerModule(
        val id: PlayerIdentityModule
) {

    companion object Factory {
        fun from(player: BurracoPlayer): BurracoPlayerModule =
                BurracoPlayerModule(
                        id = PlayerIdentityModule.from(player.identity())
                )
    }
}