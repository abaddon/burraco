package com.abaddon83.burracoGame.commands

import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
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

class AppendCardOnBurracoHandler() : CommandHandler<AppendCardOnBurracoCmd>, KoinComponent {

    private val repository: BurracoGameRepositoryPort by inject()


    override fun handle(command: AppendCardOnBurracoCmd) {
        val gameExecution = runBlocking {repository.findBurracoGameExecutionTurnExecutionBy(gameIdentity = command.gameIdentity)}
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val gameExecutionUpdated = gameExecution.appendCardsOnABurracoDropped(
                playerIdentity = command.playerIdentity,
                cardsToAppend = command.cardsToAppend,
                burracoIdentity = command.burracoIdentity )
        repository.save(gameExecutionUpdated)
    }

    override suspend fun handleAsync(command: AppendCardOnBurracoCmd) {
        val gameExecution = repository.findBurracoGameExecutionTurnExecutionBy(gameIdentity = command.gameIdentity)
        check(gameExecution != null) {"GameIdentity ${command.gameIdentity} not found exist"}
        val gameExecutionUpdated = gameExecution.appendCardsOnABurracoDropped(
                playerIdentity = command.playerIdentity,
                cardsToAppend = command.cardsToAppend,
                burracoIdentity = command.burracoIdentity )
        repository.save(gameExecutionUpdated)
    }
}