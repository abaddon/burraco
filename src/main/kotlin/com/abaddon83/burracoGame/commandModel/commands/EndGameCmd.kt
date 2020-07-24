package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
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

data class EndGameCmd(
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


class EndGameHandler() : CommandHandler<EndGameCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: EndGameCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: EndGameCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: EndGameCmd) {
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameExecutionTurnEnd -> repository.save(game
                    .completeGame(playerIdentity = command.playerIdentity))
            else -> warnMsg("GameIdentity ${command.gameIdentity} not found exist")
        }
    }
}