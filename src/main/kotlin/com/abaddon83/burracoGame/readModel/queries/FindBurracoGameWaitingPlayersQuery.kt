package com.abaddon83.burracoGame.readModel.queries

import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameWaitingQuery(val gameIdentity: GameIdentity): Query<BurracoGameWaitingPlayers?> {
}

class FindBurracoGameWaitingHandler(): QueryHandler<FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers?>, KoinComponent {

    private val repository: BurracoGameReadModelRepositoryPort by inject()
    
    override fun handle(query: FindBurracoGameWaitingQuery): BurracoGameWaitingPlayers? {
        return runBlocking { repository.findBurracoGameWaitingPlayersBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameWaitingQuery): BurracoGameWaitingPlayers? {
        return repository.findBurracoGameWaitingPlayersBy(query.gameIdentity)
    }
}