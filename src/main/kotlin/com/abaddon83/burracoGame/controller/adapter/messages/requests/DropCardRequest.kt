package com.abaddon83.burracoGame.controller.adapter.messages.requests

import com.abaddon83.burracoGame.controller.adapter.messages.CardModule

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class DropCardRequest(
        val card: CardModule
)
