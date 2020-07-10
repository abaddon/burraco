package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.BurracoScale
import com.abaddon83.burracoGame.domainModels.BurracoTris
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

data class DropTrisCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        val tris: BurracoTris,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity,playerIdentity: PlayerIdentity,tris: BurracoTris): this(
            gameIdentity = gameIdentity,
            playerIdentity = playerIdentity,
            tris = tris,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis()))
}

class DropTrisHandler() : CommandHandler<DropTrisCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()
    
    override fun handle(command: DropTrisCmd) {
        val gameExecution = runBlocking {repository.findBurracoGameExecutionTurnExecutionBy(gameIdentity = command.gameIdentity)}
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = gameExecution.dropOnTableATris(playerIdentity = command.playerIdentity, tris = command.tris)
        repository.save(updatedGameExecution)
    }

    override suspend fun handleAsync(command: DropTrisCmd) {
        val gameExecution = repository.findBurracoGameExecutionTurnExecutionBy(gameIdentity = command.gameIdentity)
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val updatedGameExecution = gameExecution.dropOnTableATris(playerIdentity = command.playerIdentity, tris = command.tris)
        repository.save(updatedGameExecution)
    }
}