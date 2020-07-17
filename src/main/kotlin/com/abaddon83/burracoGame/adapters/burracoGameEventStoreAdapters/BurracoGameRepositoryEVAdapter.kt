package com.abaddon83.burracoGame.adapters.burracoGameEventStoreAdapters

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.es.eventStore.EventStore
import com.abaddon83.utils.es.repository.EventSourcedRepository

class BurracoGameRepositoryEVAdapter(eventStore: EventStore<GameIdentity>) : EventSourcedRepository<GameIdentity, BurracoGame>(eventStore) {

    override fun new(id: GameIdentity): BurracoGame =
        BurracoGame(id)



}