package com.abaddon83.burracoGame.ports

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import java.util.*

interface BurracoGameControllerPort {
    suspend fun createNewBurracoGame(): BurracoGame
    suspend fun findBurracoGameBy(burracoGameIdentity: GameIdentity): BurracoGame?
    suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameWaitingPlayers
    suspend fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameExecution
    suspend fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameExecution
    suspend fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card): BurracoGameExecution
    suspend fun showPlayerCards(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): List<Card>
}