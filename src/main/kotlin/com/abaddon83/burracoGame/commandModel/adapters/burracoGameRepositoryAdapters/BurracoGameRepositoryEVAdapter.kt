package com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.es.eventStore.EventStore
import com.abaddon83.utils.es.repository.EventSourcedRepository

class BurracoGameRepositoryEVAdapter(eventStore: EventStore<GameIdentity>) : BurracoGameRepositoryPort, EventSourcedRepository<GameIdentity, BurracoGame>(eventStore) {


    override fun new(id: GameIdentity): BurracoGame =
            BurracoGame(id)

}