package com.abaddon83.burracoGame.queries

import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameExecutionQuery(val gameIdentity: GameIdentity): Query<BurracoGameExecution?> {
}

class FindBurracoGameExecutionHandler(): QueryHandler<FindBurracoGameExecutionQuery, BurracoGameExecution?>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()
    
    override fun handle(query: FindBurracoGameExecutionQuery): BurracoGameExecution? {
        return runBlocking { repository.findBurracoGameExecutionBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameExecutionQuery): BurracoGameExecution? {
        return repository.findBurracoGameExecutionBy(query.gameIdentity)
    }
}