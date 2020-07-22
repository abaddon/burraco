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

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Your KoinApplication instance here
        modules(testAdapters)
    }

    @Test
    fun `(async) Given a command to create a new game, when I execute the command, then a new game is created`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        runBlocking { CreateNewBurracoGameHandler().handleAsync(command) }
    }

    @Test
    fun `Given a command to create a new game, when I execute the command, then a new game is created`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        CreateNewBurracoGameHandler().handle(command)
    }

    @Test
    fun `(async) Given an existing burraco game , when I try to create it again, then I receive an exception`(){
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
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`(){
        val gameIdentity = GameIdentity.create()
        val command = CreateNewBurracoGameCmd(gameIdentity = gameIdentity)
        CreateNewBurracoGameHandler().handle(command)

        assertFailsWith(IllegalStateException::class){
            CreateNewBurracoGameHandler().handle(command)
        }

    }

    val testAdapters = module {
        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
        single< BurracoGameRepositoryPort> { repository}
    }

    //@get:Rule
    //val diRepositoryTestRule = DIRepositoryTestRule()
}