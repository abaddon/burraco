package com.abaddon83.burracoGame.commandModel.ports

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.services.CommandModelService
import com.abaddon83.utils.cqs.CommandDispatcherImpl
import com.abaddon83.utils.cqs.ContextImpl
import com.abaddon83.utils.cqs.QueryDispatcherImpl
import com.abaddon83.utils.cqs.commands.CommandDispatcher
import com.abaddon83.utils.cqs.queries.QueryDispatcher

interface BurracoGameCommandControllerPort {

    val service: CommandModelService
        get() = CommandModelService

    suspend fun createNewBurracoGame(gameIdentity: GameIdentity): Unit
    suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Unit
    suspend fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Unit
    suspend fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Unit
    suspend fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card): Unit
}