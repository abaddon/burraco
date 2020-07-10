package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnExecution
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

data class OrganisePlayerCardsCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        val orderedCards: List<Card>,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity,
                playerIdentity: PlayerIdentity,
                orderedCards: List<Card>): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            orderedCards = orderedCards,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis())
    )
}

class OrganisePlayerCardsHandler() : CommandHandler<OrganisePlayerCardsCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: OrganisePlayerCardsCmd) {
        val gameExecution = runBlocking { repository.findBurracoGameExecutionBy(gameIdentity = command.gameIdentity) }
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = reorderPlayerCards(gameExecution, command.playerIdentity, command.orderedCards)
        repository.save(updatedGameExecution)
    }

    override suspend fun handleAsync(command: OrganisePlayerCardsCmd) {
        val gameExecution = repository.findBurracoGameExecutionBy(gameIdentity = command.gameIdentity)
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = reorderPlayerCards(gameExecution, command.playerIdentity, command.orderedCards)
        repository.save(updatedGameExecution)
    }

    private fun reorderPlayerCards(game: BurracoGameExecution, playerIdentity: PlayerIdentity, orderedCards: List<Card>):BurracoGameExecution {
       return when(game){
           is BurracoGameExecutionTurnBeginning -> game.updatePlayerCardsOrder(playerIdentity, orderedCards)
           is BurracoGameExecutionTurnExecution -> game.updatePlayerCardsOrder(playerIdentity, orderedCards)
           is BurracoGameExecutionTurnEnd -> throw Exception("Not yet implemented the possibility to update card order in the turn end phase ") //game.updatePlayerCardsOrder(playerIdentity,orderedCards)
           else -> throw Exception()
        }
    }
}