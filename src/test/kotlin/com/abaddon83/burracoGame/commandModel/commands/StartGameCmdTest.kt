package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.PlayerAdded
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.testUtils.DIRepositoryTestRule
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertFailsWith

class StartGameCmdTest: KoinTest {

    val gameIdentity: GameIdentity = GameIdentity.create()
    val aggregate = BurracoGame(identity = gameIdentity)
    val playerIdentity1 = PlayerIdentity.create()
    val playerIdentity2 = PlayerIdentity.create()

    val events = listOf<Event>(
            BurracoGameCreated(gameIdentity = gameIdentity),
            PlayerAdded(gameIdentity = gameIdentity, playerIdentity = playerIdentity1),
            PlayerAdded(gameIdentity = gameIdentity, playerIdentity = playerIdentity2)
    )


    val testAdapters = module {
        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        eventStore.uploadEvents(aggregate,events)

        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
        single< BurracoGameRepositoryPort> { repository}
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testAdapters)
    }

    @Test
    fun `(async) Given a burraco game with 2 players, when I start the game, then the game is started`(){
        val command = StartGameCmd(gameIdentity = gameIdentity)
        runBlocking { StartGameHandler().handleAsync(command) }
    }

    @Test
    fun `Given a burraco game with 2 players, when I start the game, then the game is started`(){
        val command = StartGameCmd(gameIdentity = gameIdentity)
        StartGameHandler().handle(command)
    }

    @Test
    fun `Given a burraco game already started, when I start the game again, then nothing happened`(){
        val command = StartGameCmd(gameIdentity = gameIdentity)
        StartGameHandler().handle(command)
        StartGameHandler().handle(command)
    }

    @Test
    fun `Given a burraco game that not exist, when I start the game, then I receive an error`(){
        val command = StartGameCmd(gameIdentity = GameIdentity.create())
        assertFailsWith(IllegalStateException::class) {
            StartGameHandler().handle(command)
        }
    }
}