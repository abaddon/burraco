package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.PlayerNotAssigned
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
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


data class AddPlayerCmd(
        val gameIdentity: GameIdentity,
        val playerIdentityToAdd: PlayerIdentity,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp) : Command {

    constructor(gameIdentity: GameIdentity,
                playerToAdd: PlayerNotAssigned) : this(
            gameIdentity = gameIdentity,
            playerIdentityToAdd = playerToAdd.identity(),
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis())
    )

    constructor(gameIdentity: GameIdentity,
                playerIdentityToAdd: PlayerIdentity) : this(
            gameIdentity = gameIdentity,
            playerIdentityToAdd = playerIdentityToAdd,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis())
    )
}


class AddPlayerHandler() : CommandHandler<AddPlayerCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: AddPlayerCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: AddPlayerCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: AddPlayerCmd){
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameWaitingPlayers -> repository.save(game.addPlayer(PlayerInGame.create(command.playerIdentityToAdd, listOf())))
            else -> warnMsg("GameIdentity ${command.gameIdentity} not found exist")
        }
    }
}