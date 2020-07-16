package com.abaddon83.burracoGame.adapters.burracoGameRepositoryAdapters.ev

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.es.EventSourcedRepository
import com.abaddon83.utils.es.EventStore

class BurracoGameRepositoryEVAdapter(eventStore: EventStore<GameIdentity>) : EventSourcedRepository<GameIdentity, BurracoGame>(eventStore) {

    override fun new(id: GameIdentity): BurracoGame =
        BurracoGame(id)



}