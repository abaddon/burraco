package com.abaddon83.burracoGame.writeModel.ports

import com.abaddon83.burracoGame.writeModel.models.BurracoPlayer
import com.abaddon83.burracoGame.writeModel.models.PlayerNotAssigned
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity

interface PlayerPort {
        suspend fun findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): PlayerNotAssigned
        suspend fun findBurracoPlayerBy(playerIdentity: PlayerIdentity): BurracoPlayer
        suspend fun findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): PlayerInGame
}