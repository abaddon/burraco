package com.abaddon83.burracoGame.readModel.adapters.readModelRestAdapter

import com.abaddon83.burracoGame.readModel.models.ReadBurracoGame
import com.abaddon83.burracoGame.readModel.ports.ReadModelControllerPort
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort
import com.abaddon83.burracoGame.readModel.queries.GetBurracoGame
import java.util.*

class ReadModelControllerAdapter(override val readModelRepository: ReadModelRepositoryPort) : ReadModelControllerPort {

    override fun findBurracoGame(gameIdentity: UUID): ReadBurracoGame? {
        val query= GetBurracoGame(gameIdentity.toString())
        return when(val entity = queryHandle.handle(query).firstOrNull()){
            is ReadBurracoGame -> entity
            else -> null
        }
    }
}