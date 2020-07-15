package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages

import com.abaddon83.burracoGame.domainModels.BurracoPlayer

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