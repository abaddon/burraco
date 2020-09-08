package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.readModel.models.ReadBurracoGame
import com.abaddon83.burracoGame.readModel.queries.QueryHandler
import java.util.*

//import com.abaddon83.burracoGame.commandModel.models.decks.Card
//import com.abaddon83.burracoGame.readModel.models.BurracoGame
//import com.abaddon83.burracoGame.readModel.queries.FindBurracoGameQuery
//import com.abaddon83.burracoGame.readModel.queries.FindPlayerCardsQuery
//import com.abaddon83.burracoGame.readModel.services.ReadModelService
//import java.util.*
//
interface ReadModelControllerPort {
    val readModelRepository: ReadModelRepositoryPort

    val queryHandle: QueryHandler
        get() = QueryHandler(readModelRepository)

    fun findBurracoGame(gameIdentity: UUID): ReadBurracoGame?

}
////
////    val service: ReadModelService
////        get() = ReadModelService
////
// suspend fun findBurracoGame(gameIdentity: UUID): BurracoGame?
////    suspend fun showPlayerCards(gameIdentity: UUID, playerIdentity: UUID): List<Card>
////}