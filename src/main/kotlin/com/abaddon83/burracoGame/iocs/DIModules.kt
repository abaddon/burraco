package com.abaddon83.burracoGame.iocs

import com.abaddon83.burracoGame.commandModel.adapters.BurracoGameCommandControllerAdapters.BurracoGameCommandControllerAdapter
import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.readModel.adapters.burracoGameReadModelRepositoryAdapters.BurracoGameRepositoryInMemoryAdapter
import com.abaddon83.burracoGame.commandModel.adapters.playerAdapters.PlayerInMemoryAdapter
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.ports.PlayerPort
import com.abaddon83.burracoGame.readModel.adapters.burracoGameReadModelControllerAdapter.BurracoGameReadModelControllerAdapter
import com.abaddon83.burracoGame.readModel.models.BurracoGame
import com.abaddon83.burracoGame.readModel.models.BurracoGameListProjection
import com.abaddon83.burracoGame.readModel.models.BurracoGameProjection
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import com.abaddon83.utils.cqs.Context
import com.abaddon83.utils.cqs.ContextImpl
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import com.abaddon83.utils.es.readModel.DocumentStore
import com.abaddon83.utils.es.readModel.InMemoryDocumentStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val AppAdapters = module {

    //val burracoGameDataStore = InMemoryDocumentStore<BurracoGame>()


    //val burracoGameListReadModelFacade = BurracoGameListReadModel(burracoGameDataStore)

    //readmodel
    single<DocumentStore<BurracoGame>> {InMemoryDocumentStore<BurracoGame>()}
    single<BurracoGameReadModelControllerPort> { BurracoGameReadModelControllerAdapter() }
    single<BurracoGameReadModelRepositoryPort> { BurracoGameRepositoryInMemoryAdapter() }


    val burracoGameProjection = BurracoGameProjection()
    val eventBus = AsyncInMemoryBus(GlobalScope).register(burracoGameProjection)
    val eventStore = InMemoryEventStore<GameIdentity>(eventBus)

    //commandModel
    single<PlayerPort> { PlayerInMemoryAdapter() }
    single<BurracoGameCommandControllerPort> { BurracoGameCommandControllerAdapter() }
    single<BurracoGameRepositoryPort> { BurracoGameRepositoryEVAdapter(eventStore) }


}