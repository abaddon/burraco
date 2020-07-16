package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.UnsupportedEventException

data class BurracoGameExecutionTurnExecution private constructor(
        override val players: List<PlayerInGame>,
        override val playerTurn: PlayerIdentity,
        override val burracoDeck: BurracoDeck,
        override val mazzettoDecks: MazzettoDecks,
        override val discardPile: DiscardPile,
        override val identity: GameIdentity) : BurracoGameExecution(identity) {

    companion object Factory {
        fun create(identity: GameIdentity, players: List<PlayerInGame>, burracoDeck: BurracoDeck, mazzettoDecks: MazzettoDecks, discardPile: DiscardPile, playerTurn: PlayerIdentity): BurracoGameExecutionTurnExecution {
            val game = BurracoGameExecutionTurnExecution(
                    identity = identity,
                    players = players,
                    burracoDeck = burracoDeck,
                    mazzettoDecks = mazzettoDecks,
                    discardPile = discardPile,
                    playerTurn = playerTurn
            )
            game.testInvariants()
            return game
        }
    }

    fun dropOnTableATris(playerIdentity: PlayerIdentity, tris: BurracoTris): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        player.dropATris(tris)
        return applyAndQueueEvent(
                TrisDropped(gameIdentity = identity(),player = player.identity(),tris = tris)
        )
    }

    fun dropOnTableAScale(playerIdentity: PlayerIdentity, scale: BurracoScale): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        player.dropAScale(scale)
        return applyAndQueueEvent(
                ScaleDropped(gameIdentity = identity(),player = player.identity(),scale = scale)
        )
    }

    fun appendCardsOnABurracoDropped(playerIdentity: PlayerIdentity, cardsToAppend: List<Card>, burracoIdentity: BurracoIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        player.appendACardOnBurracoDropped(burracoIdentity, cardsToAppend)
        return applyAndQueueEvent(
                CardAddedOnBurraco(gameIdentity = identity(), player = player.identity(), cardsToAppend = cardsToAppend, burracoIdentity = burracoIdentity)
        )
    }

    fun pickupMazzetto(playerIdentity: PlayerIdentity): BurracoGameExecutionTurnExecution {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        check(player.showMyCards().isEmpty()) { warnMsg("The player cannot pick up a Mazzetto if he still has cards") }
        check(!player.isMazzettoTaken()) { warnMsg("The player cannot pick up a Mazzetto he already taken") }

        val mazzetto = mazzettoDecks.firstMazzettoAvailable()

        return applyAndQueueEvent(
                MazzettoPickedUp(gameIdentity = identity(),player = player.identity(), mazzettoDeck = mazzetto)
        )
    }

    fun dropCardOnDiscardPile(playerIdentity: PlayerIdentity, card: Card): BurracoGameExecutionTurnEnd {
        val player = validatePlayerId(playerIdentity)
        validatePlayerTurn(playerIdentity)
        return applyAndQueueEvent(
                CardDroppedIntoDiscardPile(gameIdentity = identity(),player = player.identity(),cardDropped = card)
        )
    }

    override fun applyEvent(event: Event): BurracoGame =
            when (event) {
                is CardDroppedIntoDiscardPile -> apply(event)
                is TrisDropped -> apply(event)
                is ScaleDropped -> apply(event)
                is CardAddedOnBurraco -> apply(event)
                is MazzettoPickedUp -> apply(event)
                else -> throw UnsupportedEventException(event::class.java)
            }

    private fun apply(event: CardDroppedIntoDiscardPile): BurracoGameExecutionTurnEnd {
        val player = players.find { p -> p.identity() == event.player }!!

        return BurracoGameExecutionTurnEnd.create(
                identity = identity(),
                players = UpdatePlayers(player.dropACard(event.cardDropped)),
                playerTurn = playerTurn,
                burracoDeck = burracoDeck,
                mazzettoDecks = mazzettoDecks,
                discardPile = discardPile.addCard(event.cardDropped)
        )
    }

    private fun apply(event: TrisDropped): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.player }!!
        return copy(
                players = UpdatePlayers(player.dropATris(event.tris))
        )
    }

    private fun apply(event: ScaleDropped): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.player }!!
        return copy(
                players = UpdatePlayers(player.dropAScale(event.scale))
        )
    }

    private fun apply(event: CardAddedOnBurraco): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.player }!!

        return copy(
                players = UpdatePlayers(player.appendACardOnBurracoDropped(event.burracoIdentity, event.cardsToAppend))
        )
    }

    private fun apply(event: MazzettoPickedUp): BurracoGameExecutionTurnExecution {
        val player = players.find { p -> p.identity() == event.player }!!

        return copy(
                players = UpdatePlayers(player.pickUpMazzetto(event.mazzettoDeck)),
                mazzettoDecks = mazzettoDecks.mazzettoTaken(event.mazzettoDeck)
        )
    }
}

//Events
data class CardDroppedIntoDiscardPile(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val cardDropped: Card,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): CardDroppedIntoDiscardPile =
            this.copy(version = version)
}

data class TrisDropped(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val tris: BurracoTris,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): TrisDropped =
            this.copy(version = version)
}

data class ScaleDropped(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val scale: BurracoScale,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): ScaleDropped =
            this.copy(version = version)
}

data class CardAddedOnBurraco(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val burracoIdentity: BurracoIdentity,
        val cardsToAppend: List<Card>,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): CardAddedOnBurraco =
            this.copy(version = version)
}

data class MazzettoPickedUp(
        val gameIdentity: GameIdentity,
        val player: PlayerIdentity,
        val mazzettoDeck: MazzettoDeck,
        val version: Long? = null) : Event(version) {
    override fun copyWithVersion(version: Long): MazzettoPickedUp =
            this.copy(version = version)
}