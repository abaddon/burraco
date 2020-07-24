package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import com.abaddon83.utils.logs.WithLog
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.sql.Timestamp
import java.util.*

data class DropCardOnDiscardPileCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        val card: Card,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity, card: Card): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            card = card,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class DropCardOnDiscardPileHandler() : CommandHandler<DropCardOnDiscardPileCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: DropCardOnDiscardPileCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: DropCardOnDiscardPileCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: DropCardOnDiscardPileCmd) {
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameExecutionTurnExecution -> repository.save(game
                    .dropCardOnDiscardPile(playerIdentity = command.playerIdentity, card = command.card))
            else -> warnMsg("GameIdentity ${command.gameIdentity} not found exist")
        }
    }
}