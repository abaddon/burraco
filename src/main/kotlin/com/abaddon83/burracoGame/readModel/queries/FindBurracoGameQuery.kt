package com.abaddon83.burracoGame.readModel.queries

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindBurracoGameQuery(val gameIdentity: GameIdentity): Query<BurracoGame?> {
}

class FindBurracoGameHandler(): QueryHandler<FindBurracoGameQuery, BurracoGame?>, KoinComponent {

    private val repository: BurracoGameReadModelRepositoryPort by inject()

    override fun handle(query: FindBurracoGameQuery): BurracoGame? {
        return runBlocking { repository.findBurracoGameBy(query.gameIdentity) }
    }

    override suspend fun handleAsync(query: FindBurracoGameQuery): BurracoGame? {
        return repository.findBurracoGameBy(query.gameIdentity)
    }
}