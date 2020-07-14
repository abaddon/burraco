package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.requests

import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.IOException
import java.util.*

//@JsonDeserialize(using = JoinGameRequestDeserializer::class)
data class JoinGameRequest(
        val playerIdentity: UUID
)

