package com.abaddon83.burracoGame.api.messages.requests

import java.util.*

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class PickUpCardRequest(
        val playerIdentity: UUID
)
