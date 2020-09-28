package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.events.BurracoGameEvent
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.ports.EventStore
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.functionals.*
import com.abaddon83.utils.logs.WithLog
import java.util.*

typealias CmdResult = Validated<DomainError, Iterable<Event>>
typealias EsScope = EventStore.() -> CmdResult


data class CommandMsg(val command: Command, val response: CmdResult) // a command with a result

class CommandHandler(val eventStore: EventStore): WithLog("CommandHandler") {

    fun handle(cmd: Command): CmdResult =
            CommandMsg(cmd, Valid(listOf())).let {
                executeCommand(it).response
            }

    private fun executeCommand(msg: CommandMsg): CommandMsg {

        val res = processPoly(msg.command)(eventStore)

        if (res is Valid) {
            eventStore.save(res.value)
        }

        //we want to reply after sending the event to the store
//        if (res is Valid) {
//            runBlocking {
//                eventStore.sendChannel.send(res.value)
//            }
//        }
        return msg.copy(response = res)
    }

    private fun processPoly(c: Command): EsScope {

        log.debug("Processing ${c}")

        val cmdResult = when (c) {
            is CreateNewBurracoGameCmd -> execute(c)
            is AddPlayerCmd -> execute(c)
            is StartGameCmd -> execute(c)
            is PickUpACardFromDeckCmd -> execute(c)
            is PickUpCardsFromDiscardPileCmd -> execute(c)
            is DropTrisCmd -> execute(c)
            is DropScaleCmd -> execute(c)
            is PickUpMazzettoDeckCmd -> execute(c)
            is AppendCardOnBurracoCmd -> execute(c)
            is DropCardOnDiscardPileCmd -> execute(c)
            is EndGameCmd -> execute(c)
            is EndPlayerTurnCmd -> execute(c)
            else -> TODO()
        }
        return cmdResult
    }

    private fun execute(c: CreateNewBurracoGameCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is EmptyBurracoGame -> Valid(BurracoGame.create(c.gameIdentity).getUncommittedChanges())
            else -> Invalid(BurracoGameError("BurracoGame ${c.gameIdentity} already exist", burracoGame))
        }
    }

    private fun execute(c: AddPlayerCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameWaitingPlayers -> Valid(burracoGame.addPlayer(PlayerInGame.create(c.playerIdentityToAdd, listOf())).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: StartGameCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameWaitingPlayers -> Valid(burracoGame.start().getUncommittedChanges())
            else -> Invalid(BurracoGameError("The game ${c.gameIdentity} doesn't exist or is in a different status, this command can not be executed", burracoGame))
        }
    }

    private fun execute(c: PickUpACardFromDeckCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnBeginning -> Valid(burracoGame.pickUpACardFromDeck(playerIdentity = c.playerIdentity).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: PickUpCardsFromDiscardPileCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnBeginning -> Valid(burracoGame.pickUpCardsFromDiscardPile(playerIdentity = c.playerIdentity).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: DropTrisCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnExecution -> Valid(burracoGame.dropOnTableATris(playerIdentity = c.playerIdentity, tris = c.tris).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: DropScaleCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnExecution -> Valid(burracoGame.dropOnTableAScale(playerIdentity = c.playerIdentity, scale = c.scale).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: PickUpMazzettoDeckCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnExecution ->
                try {
                    Valid(burracoGame.pickupMazzetto(playerIdentity = c.playerIdentity).getUncommittedChanges())
                } catch (e: Exception) {
                    Invalid(BurracoGameError(cmd = c, exception = e, burracoGame = burracoGame))
                }
            is BurracoGameExecutionTurnEnd ->
                try {
                    Valid(burracoGame.pickupMazzetto(playerIdentity = c.playerIdentity).getUncommittedChanges())
                } catch (e: Exception) {
                    Invalid(BurracoGameError(cmd = c, exception = e, burracoGame = burracoGame))
                }
            is BurracoGameExecutionTurnBeginning -> throw Exception("Not yet implemented the possibility to pickup mazzetto during the beginning phase")
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }


    private fun execute(c: AppendCardOnBurracoCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnExecution -> try {
                Valid(burracoGame.appendCardsOnABurracoDropped(playerIdentity = c.playerIdentity, cardsToAppend = c.cardsToAppend, burracoIdentity = c.burracoIdentity).getUncommittedChanges())
            } catch (e: Exception) {
                Invalid(BurracoGameError(cmd = c, exception = e, burracoGame = burracoGame))
            }
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: DropCardOnDiscardPileCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnExecution -> Valid(burracoGame.dropCardOnDiscardPile(playerIdentity = c.playerIdentity, card = c.card).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: EndGameCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnEnd -> Valid(burracoGame.completeGame(playerIdentity = c.playerIdentity).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }

    private fun execute(c: EndPlayerTurnCmd): EsScope = {
        val burracoGame = getEvents<BurracoGameEvent>(c.gameIdentity.convertTo().toString()).fold()
        when (burracoGame) {
            is BurracoGameExecutionTurnEnd -> Valid(burracoGame.nextPlayerTurn(playerIdentity = c.playerIdentity).getUncommittedChanges())
            else -> Invalid(BurracoGameError("GameIdentity ${c.gameIdentity} not found", burracoGame))
        }
    }


}

private fun List<BurracoGameEvent>.fold(): BurracoGame {
    val emptyBurracoGame = EmptyBurracoGame(GameIdentity(UUID.fromString("00000000-0000-0000-0000-000000000000")))
    return this.fold(emptyBurracoGame) { i: BurracoGame, e: BurracoGameEvent -> i.applyEvent(e) }
}

data class EmptyBurracoGame constructor(override val identity: GameIdentity) : BurracoGame(identity)