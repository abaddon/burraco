package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.testUtils.DIRepositoryTestRule
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertFailsWith

class CreateNewBurracoGameCmdTest: KoinTest {

    val testAdapters = module {
        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
        single< BurracoGameRepositoryPort> { repository}
    }

    //@get:Rule
    //val diRepositoryTestRule = DIRepositoryTestRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Your KoinApplication instance here
        modules(testAdapters)
    }

    @Test
    fun `(async) Given I receive a creation command, when I execute it, then I created a new burraco game`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        runBlocking { CreateNewBurracoGameHandler().handleAsync(command) }
    }

    @Test
    fun `Given I receive a creation command, when I execute it, then I created a new burraco game`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        CreateNewBurracoGameHandler().handle(command)
    }

    @Test
    fun `(async) Given an existing burraco game , when I try to create it again, then I receive an execption`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        assertFailsWith(IllegalStateException::class){
            runBlocking {
                CreateNewBurracoGameHandler().handleAsync(command)
                CreateNewBurracoGameHandler().handleAsync(command)
            }
        }
    }

    @Test
    fun `Given an existing burraco game , when I try to create it again, then I receive an execption`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        CreateNewBurracoGameHandler().handle(command)

        assertFailsWith(IllegalStateException::class){
            CreateNewBurracoGameHandler().handle(command)
        }

    }
}