package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.commandModel.models.decks.Card
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

data class AppendCardOnBurracoCmd(
        val gameIdentity: GameIdentity,
        val playerIdentity: PlayerIdentity,
        val burracoIdentity: BurracoIdentity,
        val cardsToAppend: List<Card>,
        override val commandId: UUID,
        override val commandTimeStamp: Timestamp
) : Command {
    constructor(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity, burracoIdentity: BurracoIdentity, cardsToAppend: List<Card>) :
            this(
                    gameIdentity = gameIdentity,
                    playerIdentity = playerIdentity,
                    burracoIdentity = burracoIdentity,
                    cardsToAppend = cardsToAppend,
                    commandId = UUID.randomUUID(),
                    commandTimeStamp = Timestamp(System.currentTimeMillis()
                    )
            )
}

class AppendCardOnBurracoHandler() : CommandHandler<AppendCardOnBurracoCmd>, KoinComponent, WithLog() {

    private val repository: BurracoGameRepositoryPort by inject()

    override fun handle(command: AppendCardOnBurracoCmd) {
        executeCmd(command)
    }

    override suspend fun handleAsync(command: AppendCardOnBurracoCmd) {
        executeCmd(command)
    }

    private fun executeCmd(command: AppendCardOnBurracoCmd) {
        when (val game = repository.getById(command.gameIdentity)) {
            is BurracoGameExecutionTurnExecution -> repository.save(game.appendCardsOnABurracoDropped(
                    playerIdentity = command.playerIdentity,
                    cardsToAppend = command.cardsToAppend,
                    burracoIdentity = command.burracoIdentity ))
            else -> warnMsg("GameIdentity ${command.gameIdentity} not found exist")
        }
    }
}