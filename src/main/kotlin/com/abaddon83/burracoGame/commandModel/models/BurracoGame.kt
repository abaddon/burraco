package com.abaddon83.burracoGame.commandModel.models

import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.models.games.Game
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.es.AggregateRoot
import com.abaddon83.utils.es.AggregateType
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException

open class BurracoGame(override val identity: GameIdentity) : Game, AggregateRoot<GameIdentity>() {
    override val maxPlayers: Int = 4
    override val minPlayers: Int = 2
    override val totalCardsRequired: Int = 108
    override val players: List<BurracoPlayer> =listOfPlayers()

    object TYPE : AggregateType {
        override fun toString() = "BurracoGame"
    }
    override fun aggregateType(): com.abaddon83.utils.es.AggregateType = TYPE

    override fun listOfPlayers(): List<BurracoPlayer> = players

    override fun applyEvent(event: Event): BurracoGame {
        log.info("apply event: ${event::class.simpleName.toString()}")
        return when (event) {
            is BurracoGameCreated -> apply(event)
            else -> throw UnsupportedEventException(event::class.java)
        }
    }


    private fun apply(event: BurracoGameCreated):BurracoGameWaitingPlayers {
        return BurracoGameWaitingPlayers(event.gameIdentity,players = event.players)
    }

    companion object Factory {
        fun create(gameIdentity: GameIdentity): BurracoGameWaitingPlayers {
            return BurracoGame(gameIdentity)
                    .applyAndQueueEvent(BurracoGameCreated(gameIdentity = gameIdentity, players = listOf()))
        }
    }
}

//Events

data class BurracoGameCreated(
        val gameIdentity: GameIdentity,
        val players: List<BurracoPlayer> = listOf(),
        val version: Long? = null) : Event(version) {
    override fun assignVersion(version: Long): BurracoGameCreated =
            this.copy(version = version)
}