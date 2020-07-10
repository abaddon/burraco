package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
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


class EndGameHandler() : CommandHandler<EndGameCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: EndGameCmd) {
        val gameEnd = runBlocking { repository.findBurracoGameExecutionTurnEndBy(gameIdentity = command.gameIdentity) }
        check(gameEnd != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameEnd = gameEnd.completeGame(playerIdentity = command.playerIdentity)
        repository.save(updatedGameEnd)
    }

    override suspend fun handleAsync(command: EndGameCmd) {
        val gameEnd = repository.findBurracoGameExecutionTurnEndBy(gameIdentity = command.gameIdentity)
        check(gameEnd != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameEnd = gameEnd.completeGame(playerIdentity = command.playerIdentity)
        repository.save(updatedGameEnd)
    }
}