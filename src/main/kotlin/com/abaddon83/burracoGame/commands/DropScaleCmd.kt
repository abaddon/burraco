package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.BurracoScale
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
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

    constructor(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity,scale: BurracoScale): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            scale = scale,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class DropScaleHandler() : CommandHandler<DropScaleCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: DropScaleCmd) {
        val gameExecution = runBlocking {repository.findBurracoGameExecutionTurnExecutionBy(gameIdentity = command.gameIdentity)}
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = gameExecution.dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale)
        repository.save(updatedGameExecution)
    }

    override suspend fun handleAsync(command: DropScaleCmd) {
        val gameExecution = repository.findBurracoGameExecutionTurnExecutionBy(gameIdentity = command.gameIdentity)
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = gameExecution.dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale)
        repository.save(updatedGameExecution)
    }
}