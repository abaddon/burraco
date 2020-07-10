package com.abaddon83.burracoGame.queries

import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameExecutionTurnExecutionQuery(val gameIdentity: GameIdentity): Query<BurracoGameExecutionTurnExecution?> {
}

class FindBurracoGameExecutionTurnExecutionHandler(): QueryHandler<FindBurracoGameExecutionTurnExecutionQuery, BurracoGameExecutionTurnExecution?>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()
    
    override fun handle(query: FindBurracoGameExecutionTurnExecutionQuery): BurracoGameExecutionTurnExecution? {
        return runBlocking { repository.findBurracoGameExecutionTurnExecutionBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameExecutionTurnExecutionQuery): BurracoGameExecutionTurnExecution? {
        return repository.findBurracoGameExecutionTurnExecutionBy(query.gameIdentity)
    }
}