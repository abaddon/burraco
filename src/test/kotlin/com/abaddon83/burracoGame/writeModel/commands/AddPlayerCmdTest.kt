package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories.EventStoreInMemory
import com.abaddon83.burracoGame.writeModel.events.BurracoGameCreated
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.functionals.Invalid
import com.abaddon83.utils.functionals.Valid
import org.junit.Before
import org.junit.Test

class AddPlayerCmdTest {

    @Before
    fun loadEvents(){
        eventStore.save(events)
    }

    @Test
    fun `Given a command to add a player to the game, when I execute the command, then the player is added`(){
        val command = AddPlayerCmd(gameIdentity = gameIdentity, playerIdentityToAdd = PlayerIdentity.create())
        assert(commandHandler.handle(command) is Valid)
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`(){
        val command = AddPlayerCmd(gameIdentity = GameIdentity.create(), playerIdentityToAdd = PlayerIdentity.create())
        assert(commandHandler.handle(command) is Invalid)
    }

    val eventStore = EventStoreInMemory()
    val gameIdentity: GameIdentity = GameIdentity.create()
    val aggregate = BurracoGame(identity = gameIdentity)
    val deck = BurracoDeck.create()
    val events = listOf<Event>(
            BurracoGameCreated(identity = gameIdentity, deck = deck.cards)
    )
    private val commandHandler = CommandHandler(eventStore)

}