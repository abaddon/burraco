package com.abaddon83.burracoGame.readModel.queries

import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameExecutionTurnEndQuery(val gameIdentity: GameIdentity): Query<BurracoGameExecutionTurnEnd?> {
}

class FindBurracoGameExecutionTurnEndHandler(): QueryHandler<FindBurracoGameExecutionTurnEndQuery, BurracoGameExecutionTurnEnd?>, KoinComponent {

    private val repository: BurracoGameReadModelRepositoryPort by inject()
    
    override fun handle(query: FindBurracoGameExecutionTurnEndQuery): BurracoGameExecutionTurnEnd? {
        return runBlocking { repository.findBurracoGameExecutionTurnEndBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameExecutionTurnEndQuery): BurracoGameExecutionTurnEnd? {
        return repository.findBurracoGameExecutionTurnEndBy(query.gameIdentity)
    }
}