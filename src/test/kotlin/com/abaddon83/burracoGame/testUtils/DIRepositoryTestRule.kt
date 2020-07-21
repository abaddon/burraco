package com.abaddon83.burracoGame.testUtils

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class DIRepositoryTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            val testAdapters = module {
                val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
                val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
                val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
                single< BurracoGameRepositoryPort> { repository}
            }

            override fun evaluate() {

                startKoin { modules(testAdapters) }

                base.evaluate()

                stopKoin()
            }
        }
    }
}