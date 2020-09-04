package com.abaddon83.burracoGame.readModel.adapters.burracoGameReadModelControllerAdapter

//import com.abaddon83.burracoGame.commandModel.models.decks.Card
//import com.abaddon83.burracoGame.readModel.models.BurracoGame
//import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
//import com.abaddon83.burracoGame.readModel.queries.FindBurracoGameQuery
//import java.util.*
//
//class BurracoGameReadModelControllerAdapter: BurracoGameReadModelControllerPort {
//
//    override suspend fun findBurracoGame(gameIdentity: UUID): BurracoGame? {
//        val query = FindBurracoGameQuery(gameIdentity = gameIdentity)
//        return service.findBurracoGameBy(query)
//    }
//
//    override suspend fun showPlayerCards(gameIdentity: UUID, playerIdentity: UUID): List<Card> {
//        //val query: FindPlayerCardsQuery()
//        //return service.findPlayerCards(query)
//        TODO()
//    }
//}