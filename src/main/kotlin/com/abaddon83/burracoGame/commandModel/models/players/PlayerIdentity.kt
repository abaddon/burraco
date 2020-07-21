package com.abaddon83.burracoGame.commandModel.models.players

import com.abaddon83.utils.ddd.UUIDIdentity
import java.util.*


data class PlayerIdentity constructor(val id: UUID) : UUIDIdentity(id) {

    companion object Factory {
        fun create(): PlayerIdentity = PlayerIdentity(UUID.randomUUID())
        fun create(uuidString: String): PlayerIdentity {
            val uuid = UUID.fromString(uuidString)
            return PlayerIdentity(uuid)
        }
    }
}