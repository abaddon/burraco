package com.abaddon83.burracoGame.readModel.adapters.burracoGameReadModelControllerAdapter

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import com.abaddon83.burracoGame.readModel.queries.FindBurracoGameQuery
import com.abaddon83.burracoGame.readModel.queries.FindPlayerCardsQuery

class BurracoGameReadModelControllerAdapter: BurracoGameReadModelControllerPort {

    override suspend fun findBurracoGame(query: FindBurracoGameQuery): BurracoGame? {
        return service.findBurracoGameBy(query)
    }

    override suspend fun showPlayerCards(query: FindPlayerCardsQuery): List<Card> {
        return service.findPlayerCards(query)
    }
}