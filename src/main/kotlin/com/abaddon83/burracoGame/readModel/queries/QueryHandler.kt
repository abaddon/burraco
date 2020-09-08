package com.abaddon83.burracoGame.readModel.queries

import com.abaddon83.burracoGame.readModel.models.ReadEntity
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort
import com.abaddon83.utils.logs.WithLog

data class QueryMsg(val query: Query, val response: List<ReadEntity>)



class QueryHandler(readModelRepository: ReadModelRepositoryPort): WithLog() {

    val repository = readModelRepository

    fun handle(q: Query):List<ReadEntity> {

        val msg = QueryMsg(q, processQuery(q))

        return msg.response

    }

    private fun processQuery(q: Query): List<ReadEntity> {
        println("Processing $q")

        return when(q){
            GetAllBurracoGames -> repository.getAllBurracoGame()
            is GetBurracoGame -> repository.get(q.identity)?.run { listOf(this)}?: emptyList()
        }
    }



}

