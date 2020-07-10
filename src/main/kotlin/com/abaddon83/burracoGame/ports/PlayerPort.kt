package com.abaddon83.burracoGame.ports

import com.abaddon83.burracoGame.domainModels.BurracoPlayer
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

interface PlayerPort {
        suspend fun findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): PlayerNotAssigned
        suspend fun findBurracoPlayerBy(playerIdentity: PlayerIdentity): BurracoPlayer
        suspend fun findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): PlayerInGame
}