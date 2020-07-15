package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import com.abaddon83.burracoGame.queries.FindBurracoGameQuery
import com.abaddon83.burracoGame.queries.FindPlayerCardsQuery
import com.abaddon83.burracoGame.services.BurracoGameService
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject
import javax.management.Query

class BurracoGameControllerRestAdapter(burracoGameService: BurracoGameService): BurracoGameControllerPort, WithLog() {

    private val service: BurracoGameService =burracoGameService

    override suspend fun createNewBurracoGame(): BurracoGame =
        service.createNewBurracoGame(GameIdentity.create())

    override suspend fun findBurracoGameBy(burracoGameIdentity: GameIdentity): BurracoGame? =
        service.executeQuery(FindBurracoGameQuery(gameIdentity = burracoGameIdentity))

    override suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameWaitingPlayers =
        service.addPlayer(gameIdentity = burracoGameIdentity,playerIdentity = playerIdentity)

    override suspend fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameExecution =
            service.startGame(gameIdentity = burracoGameIdentity)

    override suspend fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameExecution =
            service.pickUpACardFromDeck(gameIdentity = burracoGameIdentity,playerIdentity = playerIdentity)

    override suspend fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card): BurracoGameExecution =
            service.dropCardOnDiscardPile(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity, card = cardToDrop)

    override suspend fun showPlayerCards(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): List<Card> {
        val query= FindPlayerCardsQuery(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity)
        return service.executeQuery(query)
    }



}