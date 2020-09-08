package com.abaddon83.burracoGame.writeModel.ports

import com.abaddon83.burracoGame.writeModel.commands.CommandHandler
import com.abaddon83.burracoGame.writeModel.commands.DomainError
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.functionals.Validated


typealias Outcome = Validated<DomainError, OutcomeDetail>
typealias OutcomeDetail = Map<String,String>

interface WriteModelControllerPort {

    val eventStore: EventStore

    val commandHandle: CommandHandler
        get() = CommandHandler(eventStore)

    fun createNewBurracoGame(gameIdentity: GameIdentity): Outcome
    fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Outcome
    fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Outcome
    fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Outcome
    fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card): Outcome
}