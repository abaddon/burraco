package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class PlayerNotAssigned(override val identity: PlayerIdentity): BurracoPlayer() {
}
