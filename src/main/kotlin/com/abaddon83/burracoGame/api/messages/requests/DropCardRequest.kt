package com.abaddon83.burracoGame.api.messages.requests

import com.abaddon83.burracoGame.api.messages.CardModule

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class DropCardRequest(
        val card: CardModule
)
