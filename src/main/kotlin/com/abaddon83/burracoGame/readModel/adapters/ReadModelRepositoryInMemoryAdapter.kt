package com.abaddon83.burracoGame.readModel.adapters

import com.abaddon83.burracoGame.readModel.models.ReadBurracoGame
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort



class ReadModelRepositoryInMemoryAdapter: ReadModelRepositoryPort {

    val readBurracoGameRepository: MutableMap<String,ReadBurracoGame> = mutableMapOf()

    override fun save(readEntity: ReadBurracoGame) {
        readBurracoGameRepository[readEntity.identity] = readEntity
    }

    override fun get(key: String): ReadBurracoGame {
        return readBurracoGameRepository.getOrDefault(key,ReadBurracoGame("",""))
    }

    override fun getAllBurracoGame(): List<ReadBurracoGame> {
        return readBurracoGameRepository.values.toList()
    }
}