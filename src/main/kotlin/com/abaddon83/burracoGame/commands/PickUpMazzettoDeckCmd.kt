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

data class PickUpMazzettoDeckCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity,
                playerIdentity: PlayerIdentity): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis())
    )
}

class PickUpMazzettoDeckHandler() : CommandHandler<PickUpMazzettoDeckCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: PickUpMazzettoDeckCmd) {
        val gameExecution = runBlocking { repository.findBurracoGameExecutionBy(gameIdentity = command.gameIdentity) }
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = pickUpMazzettoDeck(gameExecution, command.playerIdentity)
        repository.save(updatedGameExecution)
    }

    override suspend fun handleAsync(command: PickUpMazzettoDeckCmd) {
        val gameExecution = repository.findBurracoGameExecutionBy(gameIdentity = command.gameIdentity)
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = pickUpMazzettoDeck(gameExecution, command.playerIdentity)
        repository.save(updatedGameExecution)
    }

    private fun pickUpMazzettoDeck(game: BurracoGameExecution, playerIdentity: PlayerIdentity):BurracoGameExecution {
       return when(game){
           is BurracoGameExecutionTurnBeginning -> throw Exception("Not yet implemented the possibility to update card order in the turn end phase ")
           is BurracoGameExecutionTurnExecution -> game.pickupMazzetto(playerIdentity)
           is BurracoGameExecutionTurnEnd -> game.pickupMazzetto(playerIdentity)
           else -> throw Exception()
        }
    }
}