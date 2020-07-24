package com.abaddon83.burracoGame.readModel.queries

import com.abaddon83.burracoGame.readModel.models.BurracoGame
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import com.abaddon83.utils.es.readModel.DocumentStore
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

data class FindBurracoGameQuery(val gameIdentity: UUID): Query<BurracoGame?> {
}

class FindBurracoGameHandler(): QueryHandler<FindBurracoGameQuery, BurracoGame?>, KoinComponent, WithLog() {

    private val repository: DocumentStore<BurracoGame> by inject()

    override fun handle(query: FindBurracoGameQuery): BurracoGame? {
        log.info("load game with id: ${query.gameIdentity}")
        return repository.get(query.gameIdentity.toString())
    }

    override suspend fun handleAsync(query: FindBurracoGameQuery): BurracoGame? {
        return repository.get(query.gameIdentity.toString())
    }
}