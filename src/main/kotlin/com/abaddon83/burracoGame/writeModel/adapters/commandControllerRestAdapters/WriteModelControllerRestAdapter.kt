package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters

import com.abaddon83.burracoGame.writeModel.commands.*
import com.abaddon83.burracoGame.writeModel.events.BurracoGameCreated
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.events.GameStarted
import com.abaddon83.burracoGame.writeModel.events.PlayerAdded
import com.abaddon83.burracoGame.writeModel.ports.EventStore
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.writeModel.ports.WriteModelControllerPort
import com.abaddon83.burracoGame.writeModel.ports.Outcome
import com.abaddon83.utils.functionals.Invalid
import com.abaddon83.utils.functionals.Valid


class WriteModelControllerRestAdapter(override val eventStore: EventStore) : WriteModelControllerPort {

    override fun createNewBurracoGame(gameIdentity: GameIdentity): Outcome {
        val cmdResult = commandHandle.handle(CreateNewBurracoGameCmd(gameIdentity))
        return CmdResultAdapter.toOutcome(cmdResult = cmdResult)
    }

    override fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Outcome {
        val cmdResult = commandHandle.handle(AddPlayerCmd(gameIdentity = burracoGameIdentity, playerIdentityToAdd = playerIdentity))
        return CmdResultAdapter.toOutcome(cmdResult = cmdResult)
    }

    override fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Outcome {
        val cmdResult = commandHandle.handle(StartGameCmd(gameIdentity = burracoGameIdentity))
        return CmdResultAdapter.toOutcome(cmdResult = cmdResult)
    }

    override fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Outcome {
        val cmdResult = commandHandle.handle(PickUpACardFromDeckCmd(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity))
        return CmdResultAdapter.toOutcome(cmdResult = cmdResult)
    }

    override fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card): Outcome {
        val cmdResult = commandHandle.handle(DropCardOnDiscardPileCmd(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity, card = cardToDrop))
        return CmdResultAdapter.toOutcome(cmdResult = cmdResult)
    }
}

object CmdResultAdapter{
    fun toOutcome(cmdResult: CmdResult): Outcome {
        return when(cmdResult){
            is Valid ->  Valid(convertEvent(cmdResult.value))
            is Invalid -> Invalid(cmdResult.err)
        }
    }

    private fun convertEvent(events : Iterable<Event>): Map<String,String>{
        return when(val event = events.first()){
            is BurracoGameCreated -> mapOf("gameIdentity" to event.key())
            is PlayerAdded -> mapOf(
                    "gameIdentity" to event.key(),
                    "playerIdentity" to event.playerIdentity.convertTo().toString()
            )
            is GameStarted -> mapOf("gameIdentity" to event.key())
            else -> mapOf("msg"  to "Ops.. ${event.javaClass.simpleName} event conversion is missing")
        }
    }
}