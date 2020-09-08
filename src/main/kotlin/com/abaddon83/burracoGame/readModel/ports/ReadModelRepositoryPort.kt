package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.readModel.models.ReadBurracoGame


interface ReadModelRepositoryPort {
    fun save(readEntity: ReadBurracoGame)
    fun get(key:String): ReadBurracoGame
    fun getAllBurracoGame(): List<ReadBurracoGame>
}