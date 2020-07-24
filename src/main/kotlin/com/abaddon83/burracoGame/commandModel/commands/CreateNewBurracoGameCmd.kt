package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.sql.Timestamp
import java.util.*

data class CreateNewBurracoGameCmd(
        val gameIdentity: GameIdentity,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp) : Command {

    constructor(gameIdentity: GameIdentity) : this(
            gameIdentity = gameIdentity,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class CreateNewBurracoGameHandler() : CommandHandler<CreateNewBurracoGameCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: CreateNewBurracoGameCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: CreateNewBurracoGameCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: CreateNewBurracoGameCmd) {
        check(!repository.exist(command.gameIdentity)){warnMsg("${command.gameIdentity} already exist")}
        repository.save(BurracoGame.create(command.gameIdentity))
    }
}
