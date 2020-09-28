package com.abaddon83.burracoGame.writeModel.models.games

import com.abaddon83.utils.ddd.UUIDIdentity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable( with = GameIdentityCustomSerializer::class)
data class GameIdentity constructor(val id: UUID) : UUIDIdentity(id) {

    companion object Factory {
        fun create(): GameIdentity = GameIdentity(UUID.randomUUID())
        fun create(uuidString: String): GameIdentity {
            val uuid = UUID.fromString(uuidString)
            return GameIdentity(uuid)
        }
    }
}

object GameIdentityCustomSerializer: KSerializer<GameIdentity> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GameIdentity", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: GameIdentity) {
        val string = value.convertTo().toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): GameIdentity {
        val uuidString = decoder.decodeString()
        return GameIdentity.create(uuidString)
    }
}