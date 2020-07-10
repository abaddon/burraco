package com.abaddon83.burracoGame.ports

import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.shared.games.GameIdentity

interface BurracoGameRepositoryPort {
    fun save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers
    fun save(burracoGame: BurracoGameExecutionTurnBeginning): BurracoGameExecutionTurnBeginning
    fun save(burracoGame: BurracoGameExecutionTurnExecution): BurracoGameExecutionTurnExecution
    fun save(burracoGame: BurracoGameExecutionTurnEnd): BurracoGameExecutionTurnEnd
    fun save(burracoGame: BurracoGameExecution): BurracoGameExecution
    fun save(burracoGame: BurracoGameEnded): BurracoGameEnded
    fun exists(identity: GameIdentity): Boolean
    suspend fun findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): BurracoGameWaitingPlayers
    suspend fun findBurracoGameInitialisedBy(gameIdentity: GameIdentity): BurracoGameExecution
    suspend fun findBurracoGameInitialisedTurnStartBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnBeginning
    suspend fun findBurracoGameInitialisedTurnExecutionBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnExecution
    suspend fun findBurracoGameInitialisedTurnEndBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnEnd
    suspend fun findBurracoBurracoGameEndedBy(gameIdentity: GameIdentity): BurracoGameEnded
    suspend fun findAllBurracoGameWaitingPlayers(): List<BurracoGameWaitingPlayers>
}