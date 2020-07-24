package com.abaddon83.burracoGame.commandModel.models

import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity

data class PlayerNotAssigned(override val identity: PlayerIdentity): BurracoPlayer() {
}
