package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.BurracoScale
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

data class DropScaleCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        val scale: BurracoScale,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity, scale: BurracoScale): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            scale = scale,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class DropScaleHandler() : CommandHandler<DropScaleCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: DropScaleCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: DropScaleCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: DropScaleCmd) {
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameExecutionTurnExecution -> repository.save(game
                    .dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale))
            else -> warnMsg("GameIdentity ${command.gameIdentity} not found exist")
        }
    }
}