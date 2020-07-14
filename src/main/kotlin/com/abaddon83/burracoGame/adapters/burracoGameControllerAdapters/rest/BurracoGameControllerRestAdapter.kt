package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import com.abaddon83.burracoGame.queries.FindBurracoGameQuery
import com.abaddon83.burracoGame.services.BurracoGameService
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject
import javax.management.Query

class BurracoGameControllerRestAdapter(burracoGameService: BurracoGameService): BurracoGameControllerPort, /*KoinComponent,*/ WithLog() {

    private val service: BurracoGameService =burracoGameService//by inject()

    override suspend fun createNewBurracoGame(): BurracoGame =
        service.createNewBurracoGame(GameIdentity.create())

    override suspend fun findBurracoGameBy(burracoGameIdentity: GameIdentity): BurracoGame? =
        service.executeQuery(FindBurracoGameQuery(gameIdentity = burracoGameIdentity))

    override suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity): BurracoGameWaitingPlayers =
        service.addPlayer(gameIdentity = burracoGameIdentity,playerIdentity = playerIdentity)



}