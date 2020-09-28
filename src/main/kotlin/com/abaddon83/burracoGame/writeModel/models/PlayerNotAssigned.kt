package com.abaddon83.burracoGame.writeModel.models

import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity

data class PlayerNotAssigned(override val identity: PlayerIdentity): BurracoPlayer("PlayerNotAssigned") {
}
