package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.BurracoTris
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

data class DropTrisCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        val tris: BurracoTris,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity, tris: BurracoTris): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            tris = tris,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class DropTrisHandler() : CommandHandler<DropTrisCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()
    
    override fun handle(command: DropTrisCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: DropTrisCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: DropTrisCmd) {
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameExecutionTurnExecution -> repository.save(game
                    .dropOnTableATris(playerIdentity = command.playerIdentity, tris = command.tris))
            else -> warnMsg("GameIdentity ${command.gameIdentity} not found exist")
        }
    }
}