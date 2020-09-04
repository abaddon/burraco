package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import org.junit.Test

class BurracoGameTest {

    @Test
    fun `Given anew game identity, when I create a new Burraco game, then I have a new game`(){
        val gameId= GameIdentity.create()
        val game = BurracoGame.create(gameId)
        assert(game.numPlayers() == 0)
        assert(game.identity() == gameId)
    }
}