package com.abaddon83.burracoGame.api.messages

import com.abaddon83.burracoGame.readModel.models.BurracoPlayer
import java.util.*


data class BurracoPlayerModule(
        val id: UUID
) {

    companion object Factory {
        fun from(player: BurracoPlayer): BurracoPlayerModule =
                BurracoPlayerModule(
                        id = player.identity
                )
    }
}