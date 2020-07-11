package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.adapters.burracoGameRepositoryAdapters.inMemories.BurracoGameDB
import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import com.abaddon83.utils.logs.WithLog
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.sql.Timestamp
import java.util.*

data class CreateNewBurracoGameCmd(
        val gameIdentity: GameIdentity,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp):Command {

    constructor(gameIdentity: GameIdentity): this(
            gameIdentity = gameIdentity,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))

}

class CreateNewBurracoGameHandler() : CommandHandler<CreateNewBurracoGameCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: CreateNewBurracoGameCmd) {
        createBurracoGame(command)
    }

    override suspend fun handleAsync(command: CreateNewBurracoGameCmd) {
        createBurracoGame(command)
    }

    private fun createBurracoGame(command: CreateNewBurracoGameCmd){
        check(!repository.exists(command.gameIdentity)) {
            log.warn("${command.gameIdentity} already exist")
            "${command.gameIdentity} already exist"
        }
        val burracoGameWaitingPlayer = BurracoGame.create(command.gameIdentity)
        repository.save(burracoGameWaitingPlayer)
        log.info("${command.gameIdentity} created")
    }
}
