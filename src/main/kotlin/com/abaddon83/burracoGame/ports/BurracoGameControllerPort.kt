package com.abaddon83.burracoGame.ports

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import java.util.*

interface BurracoGameControllerPort {
    suspend fun createNewBurracoGame(): BurracoGame
    suspend fun findBurracoGameBy(burracoGameIdentity: GameIdentity): BurracoGame?
    suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameWaitingPlayers
}