package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity

interface BurracoGameReadModelControllerPort {
    suspend fun findBurracoGameBy(burracoGameIdentity: GameIdentity): BurracoGame?
    suspend fun showPlayerCards(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): List<Card>
}