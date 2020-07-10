package com.abaddon83.burracoGame.shared.burracos

import com.abaddon83.utils.ddd.UUIDIdentity
import java.util.*


data class BurracoIdentity private constructor(private val id: UUID) : UUIDIdentity(id) {

    companion object Factory {
        fun create(): BurracoIdentity = BurracoIdentity(UUID.randomUUID())
        fun create(uuidString: String): BurracoIdentity {
            val uuid = UUID.fromString(uuidString)
            return BurracoIdentity(uuid)
        }
    }
}