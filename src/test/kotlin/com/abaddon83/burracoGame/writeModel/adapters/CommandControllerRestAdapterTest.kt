package com.abaddon83.burracoGame.writeModel.adapters

import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.CommandControllerRestAdapter
import com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories.EventStoreInMemory
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.functionals.Invalid
import org.junit.Test


class CommandControllerRestAdapterTest {

    @Test
    fun `Given a game identity wrong, when I join a new player, then I receive an error`(){
        val outcome = commandController.joinPlayer(GameIdentity.create(), PlayerIdentity.create())
        assert(outcome is Invalid)

    }

    val commandController = CommandControllerRestAdapter(eventStore = EventStoreInMemory())

}