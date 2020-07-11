package com.abaddon83.burracoGame.ports

import com.abaddon83.burracoGame.domainModels.BurracoGame

interface BurracoGameControllerPort {
    suspend fun createNewBurracoGame(): BurracoGame
}