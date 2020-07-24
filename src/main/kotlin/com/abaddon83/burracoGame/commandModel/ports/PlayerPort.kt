package com.abaddon83.burracoGame.commandModel.ports

import com.abaddon83.burracoGame.commandModel.models.BurracoPlayer
import com.abaddon83.burracoGame.commandModel.models.PlayerNotAssigned
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity

interface PlayerPort {
        suspend fun findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): PlayerNotAssigned
        suspend fun findBurracoPlayerBy(playerIdentity: PlayerIdentity): BurracoPlayer
        suspend fun findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): PlayerInGame
}