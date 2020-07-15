package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import kotlinx.coroutines.runBlocking
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

class StartGameHandler() : CommandHandler<StartGameCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: StartGameCmd) {
        val burracoGameWaitingPlayers = runBlocking { repository.findBurracoGameWaitingPlayersBy(command.gameIdentity) }
        checkNotNull(burracoGameWaitingPlayers) {"GameIdentity ${command.gameIdentity} not found exist"}
        repository.save(burracoGameWaitingPlayers.start())
    }

    override suspend fun handleAsync(command: StartGameCmd) {
        val burracoGameWaitingPlayers = repository.findBurracoGameWaitingPlayersBy(command.gameIdentity)
        checkNotNull(burracoGameWaitingPlayers) {"GameIdentity ${command.gameIdentity} not found exist"}
        repository.save(burracoGameWaitingPlayers.start())
    }
}