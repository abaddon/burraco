package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.services.CommandModelService
import com.abaddon83.burracoGame.readModel.queries.FindBurracoGameQuery
import com.abaddon83.burracoGame.readModel.queries.FindPlayerCardsQuery
import com.abaddon83.burracoGame.readModel.services.ReadModelService

interface BurracoGameReadModelControllerPort {

    val service: ReadModelService
        get() = ReadModelService

    suspend fun findBurracoGame(query: FindBurracoGameQuery): BurracoGame?
    suspend fun showPlayerCards(query: FindPlayerCardsQuery): List<Card>
}