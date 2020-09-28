package com.abaddon83.burracoGame.writeModel.models

import com.abaddon83.burracoGame.writeModel.events.BurracoGameCreated
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.writeModel.models.games.Game
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.utils.es.AggregateRoot
import com.abaddon83.utils.es.AggregateType
import com.abaddon83.utils.es.UnsupportedEventException

open class BurracoGame(override val identity: GameIdentity, className:String) : Game, AggregateRoot<GameIdentity>(className) {
    constructor(identity: GameIdentity): this(identity,"BurracoGame")
    override val maxPlayers: Int = 4
    override val minPlayers: Int = 2
    override val totalCardsRequired: Int = 108
    override val players: List<BurracoPlayer> =listOf()
    override val deck: BurracoDeck = BurracoDeck.create(listOf())

    object TYPE : AggregateType {
        override fun toString() = "BurracoGame"
    }
    override fun aggregateType(): AggregateType = TYPE

    override fun listOfPlayers(): List<BurracoPlayer> = players

    companion object Factory {
        fun create(gameIdentity: GameIdentity): BurracoGameWaitingPlayers {
            val listCards=BurracoDeck.create().cards
            return BurracoGame(gameIdentity)
                    .applyAndQueueEvent(BurracoGameCreated(gameIdentity,listCards))
        }
    }


    override fun applyEvent(event: Event): BurracoGame {
        log.info("apply event: ${event::class.simpleName.toString()}")
        return when (event) {
            is BurracoGameCreated -> apply(event)
            else -> throw UnsupportedEventException(event::class.java)
        }
    }
    private fun apply(event: BurracoGameCreated):BurracoGameWaitingPlayers {
        val burracoDeck = BurracoDeck.create(event.deck)
        return BurracoGameWaitingPlayers(event.identity, listOf(), burracoDeck)
    }
}