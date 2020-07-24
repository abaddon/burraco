package com.abaddon83.burracoGame.commandModel.services

import com.abaddon83.burracoGame.commandModel.commands.*
import com.abaddon83.utils.cqs.CommandDispatcherImpl
import com.abaddon83.utils.cqs.ContextImpl
import com.abaddon83.utils.cqs.commands.CommandDispatcher
import com.abaddon83.utils.logs.WithLog

object CommandModelService: WithLog() {
    private val cqsPackages = listOf("com.abaddon83.burracoGame.commandModel.commands")
    private val commandDispatcher: CommandDispatcher = CommandDispatcherImpl(ContextImpl(cqsPackages))

    suspend fun createNewBurracoGame(command: CreateNewBurracoGameCmd): Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun addPlayer(command: AddPlayerCmd): Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun startGame(command: StartGameCmd): Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun pickUpACardFromDeck(command: PickUpACardFromDeckCmd): Unit{
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun pickUpCardsFromDiscardPile(command: PickUpCardsFromDiscardPileCmd): Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun dropScale(command: DropScaleCmd) : Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun dropTris(command: DropTrisCmd) : Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun appendCardOnBurraco(command: AppendCardOnBurracoCmd) : Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun pickUpMazzettoDeck(command: PickUpMazzettoDeckCmd) : Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun dropCardOnDiscardPile(command: DropCardOnDiscardPileCmd) : Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun endPlayerTurn(command: EndPlayerTurnCmd): Unit {
        commandDispatcher.dispatchAsync(command)
    }

    suspend fun endGame(command: EndGameCmd): Unit {
        commandDispatcher.dispatchAsync(command)
    }



}