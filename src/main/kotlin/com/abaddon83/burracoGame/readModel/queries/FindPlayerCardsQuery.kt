package com.abaddon83.burracoGame.readModel.queries

import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import com.abaddon83.utils.logs.WithLog
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

data class FindPlayerCardsQuery(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity): Query<List<Card>> {
}

class FindPlayerCardsHandler(): QueryHandler<FindPlayerCardsQuery, List<Card>>, KoinComponent, WithLog() {

    private val repository: BurracoGameReadModelRepositoryPort by inject()
    
    override fun handle(query: FindPlayerCardsQuery): List<Card> {
        return runBlocking {
            val game = checkNotNull(repository.findBurracoGameExecutionBy(query.gameIdentity)){warnMsg("${query.gameIdentity} not found")}
            game.playerCards(query.playerIdentity)
        }
    }

    override suspend fun handleAsync(query: FindPlayerCardsQuery): List<Card> {
        val game = checkNotNull(repository.findBurracoGameExecutionBy(query.gameIdentity)){warnMsg("${query.gameIdentity} not found")}
        return game.playerCards(query.playerIdentity)
    }
}