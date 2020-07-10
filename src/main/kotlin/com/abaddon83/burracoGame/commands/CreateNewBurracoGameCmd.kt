package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
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

class CreateNewBurracoGameHandler() : CommandHandler<CreateNewBurracoGameCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: CreateNewBurracoGameCmd) {
        check(repository.exists(command.gameIdentity)) {"GameIdentity ${command.gameIdentity} already exist"}
        val burracoGameWaitingPlayer = BurracoGame.create(command.gameIdentity)
        repository.save(burracoGameWaitingPlayer)
    }

    override suspend fun handleAsync(command: CreateNewBurracoGameCmd) {
        check(repository.exists(command.gameIdentity)) {"GameIdentity ${command.gameIdentity} already exist"}
        val burracoGameWaitingPlayer = BurracoGame.create(command.gameIdentity)
        repository.save(burracoGameWaitingPlayer)
    }
}
