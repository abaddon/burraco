package com.abaddon83.burracoGame.controller.adapter

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.readModel.queries.FindBurracoGameQuery
import com.abaddon83.burracoGame.readModel.queries.FindPlayerCardsQuery
import com.abaddon83.burracoGame.services.BurracoGameService
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import com.abaddon83.utils.logs.WithLog

class BurracoGameControllerRestAdapter(burracoGameService: BurracoGameService): BurracoGameReadModelControllerPort, BurracoGameCommandControllerPort, WithLog() {

    private val service: BurracoGameService =burracoGameService

    override suspend fun createNewBurracoGame(): Unit {
        service.createNewBurracoGame(GameIdentity.create())
    }


    override suspend fun findBurracoGameBy(burracoGameIdentity: GameIdentity): BurracoGame? =
        service.executeQuery(FindBurracoGameQuery(gameIdentity = burracoGameIdentity))

    override suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Unit {
        service.addPlayer(gameIdentity = burracoGameIdentity,playerIdentity = playerIdentity)
    }


    override suspend fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Unit {
        service.startGame(gameIdentity = burracoGameIdentity)
    }


    override suspend fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Unit {
        service.pickUpACardFromDeck(gameIdentity = burracoGameIdentity,playerIdentity = playerIdentity)
    }


    override suspend fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card): Unit {
        service.dropCardOnDiscardPile(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity, card = cardToDrop)
    }


    override suspend fun showPlayerCards(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): List<Card> {
        val query= FindPlayerCardsQuery(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity)
        return service.executeQuery(query)
    }



}