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


data class AddPlayerCmd(
        val gameIdentity: GameIdentity,
        val playerToAdd: PlayerNotAssigned,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp): Command {

    constructor(gameIdentity: GameIdentity,
                playerToAdd: PlayerNotAssigned): this(
            gameIdentity = gameIdentity,
            playerToAdd = playerToAdd,
            commandId = UUID.randomUUID(),
            commandTimeStamp = Timestamp(System.currentTimeMillis())
    )
}


class AddPlayerHandler() : CommandHandler<AddPlayerCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()


    override fun handle(command: AddPlayerCmd) {
        val burracoGameWaitingPlayers = runBlocking { repository.findBurracoGameWaitingPlayersBy(command.gameIdentity) }
        check(burracoGameWaitingPlayers != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        repository.save(burracoGameWaitingPlayers.addPlayer(command.playerToAdd))
    }

    override suspend fun handleAsync(command: AddPlayerCmd) {
        val burracoGameWaitingPlayers = repository.findBurracoGameWaitingPlayersBy(command.gameIdentity)
        check(burracoGameWaitingPlayers != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        repository.save(burracoGameWaitingPlayers.addPlayer(command.playerToAdd))
    }
}