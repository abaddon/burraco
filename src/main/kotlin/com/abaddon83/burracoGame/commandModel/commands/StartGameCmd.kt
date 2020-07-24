package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.sql.Timestamp
import java.util.*


data class StartGameCmd(
        val gameIdentity: GameIdentity,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity): this(
            gameIdentity = gameIdentity,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis())
    )
}

class StartGameHandler() : CommandHandler<StartGameCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: StartGameCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: StartGameCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: StartGameCmd) {
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameWaitingPlayers -> repository.save(game
                    .start())
            else -> warnMsg("The game ${command.gameIdentity} doesn't exist or is in a different status, this command can not be executed")
        }
    }
}