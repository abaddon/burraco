package com.abaddon83.burracoGame.queries

import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameExecutionTurnBeginningQuery(val gameIdentity: GameIdentity): Query<BurracoGameExecutionTurnBeginning?> {
}

class FindBurracoGameExecutionTurnBeginningHandler(): QueryHandler<FindBurracoGameExecutionTurnBeginningQuery, BurracoGameExecutionTurnBeginning?>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()
    
    override fun handle(query: FindBurracoGameExecutionTurnBeginningQuery): BurracoGameExecutionTurnBeginning? {
        return runBlocking { repository.findBurracoGameExecutionTurnBeginningBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameExecutionTurnBeginningQuery): BurracoGameExecutionTurnBeginning? {
        return repository.findBurracoGameExecutionTurnBeginningBy(query.gameIdentity)
    }
}