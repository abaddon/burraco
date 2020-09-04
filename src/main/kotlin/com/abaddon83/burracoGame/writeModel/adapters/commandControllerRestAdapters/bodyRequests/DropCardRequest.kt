package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.bodyRequests

import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.messages.CardModule

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class DropCardRequest(
        val card: CardModule
)
