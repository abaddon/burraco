package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories.EventStoreInMemory
import com.abaddon83.burracoGame.writeModel.events.BurracoGameCreated
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.events.PlayerAdded
import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.functionals.Invalid
import com.abaddon83.utils.functionals.Valid
import org.junit.Before
import org.junit.Test

class StartGameCmdTest {

    @Before
    fun loadEvents(){
        eventStore.save(events)
    }

    @Test
    fun `Given a command to start a game, when I execute the command, then the game is started`(){
        val command = StartGameCmd(gameIdentity = gameIdentity)
        assert(commandHandler.handle(command) is Valid)
    }

    @Test
    fun `Given a burraco game already started, when I start the game again, then nothing happened`(){
        val command = StartGameCmd(gameIdentity = gameIdentity)
        assert(commandHandler.handle(command) is Valid)
        assert(commandHandler.handle(command) is Invalid)
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`(){
        val command = StartGameCmd(gameIdentity = GameIdentity.create())
        assert(commandHandler.handle(command) is Invalid)
    }

    val eventStore = EventStoreInMemory()
    private val commandHandler = CommandHandler(eventStore)
    val deck = BurracoDeck.create()
    val gameIdentity: GameIdentity = GameIdentity.create()
    val aggregate = BurracoGame(identity = gameIdentity)
    val playerIdentity1 = PlayerIdentity.create()
    val playerIdentity2 = PlayerIdentity.create()

    val events = listOf<Event>(
            BurracoGameCreated(identity = gameIdentity, deck = deck.cards),
            PlayerAdded(identity = gameIdentity, playerIdentity = playerIdentity1),
            PlayerAdded(identity = gameIdentity, playerIdentity = playerIdentity2)
    )
}