package com.abaddon83.burracoGame.writeModel.adapters

import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.WriteModelControllerRestAdapter
import com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories.EventStoreInMemoryAdapter
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

    val commandController = WriteModelControllerRestAdapter(eventStore = EventStoreInMemoryAdapter())

}