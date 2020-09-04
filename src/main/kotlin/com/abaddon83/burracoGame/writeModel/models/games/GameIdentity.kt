package com.abaddon83.burracoGame.writeModel.models.games

import com.abaddon83.utils.ddd.UUIDIdentity
import java.util.*

data class GameIdentity constructor(val id: UUID) : UUIDIdentity(id) {

    companion object Factory {
        fun create(): GameIdentity = GameIdentity(UUID.randomUUID())
        fun create(uuidString: String): GameIdentity {
            val uuid = UUID.fromString(uuidString)
            return GameIdentity(uuid)
        }
    }
}