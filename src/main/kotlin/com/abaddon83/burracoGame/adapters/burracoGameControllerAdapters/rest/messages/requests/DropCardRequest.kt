package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.requests

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.CardModule
import java.util.*

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class DropCardRequest(
        val card: CardModule
)
