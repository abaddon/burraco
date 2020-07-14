package com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.burracoGame.testUtils.BurracoGameInitTurnTestFactory
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoGameWaitingPlayersTest {
    @Test
    fun `Given a burraco game, when I add a new player, then the num of player increase`(){

        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer()

        val newGame = game.addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        assert(newGame.identity() == game.identity())
        assert(newGame.numPlayers() == game.numPlayers() + 1)
    }
    @Test
    fun `Given a burraco game, when I add a player 2 times, then I receive an error`(){
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer()
        val player1 = PlayerNotAssigned(PlayerIdentity.create())
        val newGame = game.addPlayer(player1)
        assertFailsWith(IllegalStateException::class){
            newGame.addPlayer(player1)
        }

        assert(newGame.identity() == game.identity())
        assert(newGame.numPlayers() == game.numPlayers() + 1)
    }
    @Test
    fun `Given a burraco game with 3 players, when I add the fourth player, then I have a game with 4 players`(){
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer()
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        val actualGame = game.addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        assert(actualGame.numPlayers() == 4)
    }
    @Test
    fun `Given a burraco game with 4 players, when I add the fifth player, then I receive an error`(){
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer()
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        assertFailsWith(IllegalStateException::class){
            game.addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
        }
    }
    @Test
    fun `Given a burraco game with 3 players, when I check if a player is in the game, then I receive a positive feedback`(){
        val player1 = PlayerNotAssigned(PlayerIdentity.create())
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer().addPlayer(player1)

        val expectedResult = true

        assert(game.isAlreadyAPlayer(player1.identity()) == expectedResult)
    }
    @Test
    fun `Given a burraco game with 2 players, when I check if a new player is in the game, then I receive a negative feedback`(){
        val player1 = PlayerNotAssigned(PlayerIdentity.create())
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer()

        val expectedResult = false

        assert(game.isAlreadyAPlayer(player1.identity()) == expectedResult)
    }
    @Test
    fun `Given a burraco game with 2 players, when I start the game, then I started the burraco game`(){
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer()
        game.start()

    }
    @Test
    fun `Given a burraco game with 1 players, when I start the game, then I receive an error`(){
        val game = BurracoGameInitTurnTestFactory.create().buildWaitingPlayer(true)
        assertFailsWith(IllegalStateException::class){
            game.start()
        }
    }
    @Test
    fun `initialise a game with 2 players`(){
        val totalCardsInGameExpected = 108

        val game = BurracoGame.create(GameIdentity.create())
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))
                .addPlayer(PlayerNotAssigned(PlayerIdentity.create()))

        val burracoCardsDealt: BurracoCardsDealt = BurracoDealer.create(game)

        val gameInitiated = game.start()

        assert(gameInitiated.numPlayers() == game.numPlayers())
        //assert(gameInitiated.listOfPlayers().exists(playerInGame => game.listOfPlayers().exists(playerWaiting => playerWaiting.playerIdentity == playerInGame.playerIdentity)))
        //TODO check this test
        //assert(gameInitiated.listOfPlayers().containsAll(game.listOfPlayers()) && game.listOfPlayers().containsAll(gameInitiated.listOfPlayers()))
        assert(gameInitiated.identity() == game.identity())

    }

}