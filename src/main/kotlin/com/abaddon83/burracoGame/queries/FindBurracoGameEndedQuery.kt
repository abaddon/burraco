package com.abaddon83.burracoGame.queries

import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameEndedQuery(val gameIdentity: GameIdentity): Query<BurracoGameEnded?> {
}

class FindBurracoGameEndedHandler(): QueryHandler<FindBurracoGameEndedQuery, BurracoGameEnded?>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(query: FindBurracoGameEndedQuery): BurracoGameEnded? {
        return runBlocking { repository.findBurracoGameEndedBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameEndedQuery): BurracoGameEnded? {
        return repository.findBurracoGameEndedBy(query.gameIdentity)
    }
}