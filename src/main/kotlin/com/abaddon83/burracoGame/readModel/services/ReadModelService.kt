package com.abaddon83.burracoGame.readModel.services

import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.readModel.models.BurracoGame
import com.abaddon83.burracoGame.readModel.queries.FindBurracoGameQuery
import com.abaddon83.burracoGame.readModel.queries.FindPlayerCardsQuery
import com.abaddon83.utils.cqs.ContextImpl
import com.abaddon83.utils.cqs.QueryDispatcherImpl
import com.abaddon83.utils.cqs.queries.QueryDispatcher
import com.abaddon83.utils.logs.WithLog

object ReadModelService: WithLog() {
    private val cqsPackages = listOf("com.abaddon83.burracoGame.readModel.queries")
    private val queryDispatcher: QueryDispatcher = QueryDispatcherImpl(ContextImpl(cqsPackages))

    fun findBurracoGameBy(query: FindBurracoGameQuery): BurracoGame? {
        return queryDispatcher.dispatch(query)
    }

    fun findPlayerCards(query: FindPlayerCardsQuery): List<Card> {  //TOFIX read model not used
        return queryDispatcher.dispatch(query)
    }




}