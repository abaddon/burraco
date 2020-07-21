package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.models.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity

interface BurracoGameReadModelRepositoryPort {
    fun save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers
    fun save(burracoGame: BurracoGameExecutionTurnBeginning): BurracoGameExecutionTurnBeginning
    fun save(burracoGame: BurracoGameExecutionTurnExecution): BurracoGameExecutionTurnExecution
    fun save(burracoGame: BurracoGameExecutionTurnEnd): BurracoGameExecutionTurnEnd
    fun save(burracoGame: BurracoGameExecution): BurracoGameExecution
    fun save(burracoGame: BurracoGameEnded): BurracoGameEnded
    fun exists(identity: GameIdentity): Boolean
    suspend fun findBurracoGameBy(gameIdentity: GameIdentity): BurracoGame?
    suspend fun findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): BurracoGameWaitingPlayers?
    suspend fun findBurracoGameExecutionBy(gameIdentity: GameIdentity): BurracoGameExecution?
    suspend fun findBurracoGameExecutionTurnBeginningBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnBeginning?
    suspend fun findBurracoGameExecutionTurnExecutionBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnExecution?
    suspend fun findBurracoGameExecutionTurnEndBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnEnd?
    suspend fun findBurracoGameEndedBy(gameIdentity: GameIdentity): BurracoGameEnded?
    suspend fun findAllBurracoGameWaitingPlayers(): List<BurracoGameWaitingPlayers>?
}