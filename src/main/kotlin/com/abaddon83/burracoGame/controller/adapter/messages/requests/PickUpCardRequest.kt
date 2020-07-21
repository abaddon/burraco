package com.abaddon83.burracoGame.controller.adapter.messages.requests

import java.util.*

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class PickUpCardRequest(
        val playerIdentity: UUID
)
