package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.bodyRequests

import java.util.*

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class PickUpCardRequest(
        val playerIdentity: UUID
)
