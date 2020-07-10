package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.BurracoScale
import com.abaddon83.burracoGame.domainModels.BurracoTris
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.sql.Timestamp
import java.util.*

data class PickUpCardsFromDiscardPileCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class PickUpCardsFromDiscardPileHandler() : CommandHandler<PickUpCardsFromDiscardPileCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()
    
    override fun handle(command: PickUpCardsFromDiscardPileCmd) {
        val gameExecutionBeginning = runBlocking {repository.findBurracoGameExecutionTurnBeginningBy(gameIdentity = command.gameIdentity)}
        check(gameExecutionBeginning != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val gameExecution = gameExecutionBeginning.pickUpCardsFromDiscardPile(playerIdentity = command.playerIdentity)
        repository.save(gameExecution)
    }

    override suspend fun handleAsync(command: PickUpCardsFromDiscardPileCmd) {
        val gameExecutionBeginning = repository.findBurracoGameExecutionTurnBeginningBy(gameIdentity = command.gameIdentity)
        check(gameExecutionBeginning != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val gameExecution = gameExecutionBeginning.pickUpCardsFromDiscardPile(playerIdentity = command.playerIdentity)
        repository.save(gameExecution)
    }
}