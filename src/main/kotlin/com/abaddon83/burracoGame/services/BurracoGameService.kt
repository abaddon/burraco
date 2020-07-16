package com.abaddon83.burracoGame.services

import com.abaddon83.burracoGame.commands.*
import com.abaddon83.burracoGame.domainModels.BurracoScale
import com.abaddon83.burracoGame.domainModels.BurracoTris
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.ports.PlayerPort
import com.abaddon83.burracoGame.queries.*
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.cqs.QueryDispatcherImpl
import com.abaddon83.utils.cqs.CommandDispatcherImpl
import com.abaddon83.utils.cqs.Context
import com.abaddon83.utils.cqs.commands.CommandDispatcher
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryDispatcher
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject


class BurracoGameService : KoinComponent, WithLog() {
    private val playerPort: PlayerPort by inject()
    private val context: Context by inject()
    private val queryDispatcher: QueryDispatcher = QueryDispatcherImpl(context)
    private val commandDispatcher: CommandDispatcher = CommandDispatcherImpl(context)

    suspend fun <TQuery : Query<TResponse>, TResponse>executeQuery(query: TQuery): TResponse {
        return queryDispatcher.dispatchAsync(query)
    }

    suspend fun createNewBurracoGame(gameIdentity: GameIdentity): BurracoGameWaitingPlayers {
        val command = CreateNewBurracoGameCmd(gameIdentity)
        val query = FindBurracoGameWaitingQuery(gameIdentity)
        commandDispatcher.dispatchAsync(command)

        return checkNotNull(queryDispatcher.dispatch(query))
        { "Game $gameIdentity not created" }
    }

    suspend fun addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameWaitingPlayers {
        val player = requireNotNull(playerPort.findPlayerNotAssignedBy(playerIdentity)){
            warnMsg("$playerIdentity not available") }
        val command = AddPlayerCmd(gameIdentity, player)
        commandDispatcher.dispatch(command)
        val query = FindBurracoGameWaitingQuery(gameIdentity)
        return checkNotNull(queryDispatcher.dispatch(query))
        { "Game $gameIdentity not found" }
    }

    suspend fun startGame(gameIdentity: GameIdentity): BurracoGameExecutionTurnBeginning{
        val command = StartGameCmd(gameIdentity)
        val query = FindBurracoGameExecutionTurnBeginningQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
        { "Game $gameIdentity not found" }
    }

    suspend fun pickUpACardFromDeck(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution{
        val command = PickUpACardFromDeckCmd(gameIdentity, playerIdentity)
        val query = FindBurracoGameExecutionTurnExecutionQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun pickUpCardsFromDiscardPile(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution{
        val command = PickUpCardsFromDiscardPileCmd(gameIdentity, playerIdentity)
        val query = FindBurracoGameExecutionTurnExecutionQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun dropScale(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, scale: BurracoScale) : BurracoGameExecutionTurnExecution{
        val command = DropScaleCmd(gameIdentity, playerIdentity,scale)
        val query = FindBurracoGameExecutionTurnExecutionQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun dropTris(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, tris: BurracoTris) : BurracoGameExecutionTurnExecution{
        val command = DropTrisCmd(gameIdentity, playerIdentity,tris)
        val query = FindBurracoGameExecutionTurnExecutionQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun appendCardOnBurraco(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity, burracoIdentity: BurracoIdentity, cardsToAppend: List<Card>) : BurracoGameExecutionTurnExecution{
        val command = AppendCardOnBurracoCmd(gameIdentity, playerIdentity,burracoIdentity,cardsToAppend)
        val query = FindBurracoGameExecutionTurnExecutionQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun pickUpMazzettoDeck(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity) : BurracoGameExecution{
        val command = PickUpMazzettoDeckCmd(gameIdentity, playerIdentity)
        val query = FindBurracoGameExecutionQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun dropCardOnDiscardPile(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity, card: Card) : BurracoGameExecutionTurnEnd {
        val command = DropCardOnDiscardPileCmd(gameIdentity, playerIdentity,card)
        val query = FindBurracoGameExecutionTurnEndQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun endPlayerTurn(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): BurracoGameExecutionTurnBeginning{
        val command = EndPlayerTurnCmd(gameIdentity, playerIdentity)
        val query = FindBurracoGameExecutionTurnBeginningQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

    suspend fun endGame(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): BurracoGameEnded {
        val command = EndGameCmd(gameIdentity, playerIdentity)
        val query = FindBurracoGameEndedQuery(gameIdentity)

        commandDispatcher.dispatchAsync(command)
        return checkNotNull(queryDispatcher.dispatch(query))
    }

}
