package com.abaddon83.burracoGame.writeModel.models.burracos

import com.abaddon83.utils.ddd.UUIDIdentity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable( with = BurracoIdentityCustomSerializer::class)
data class BurracoIdentity private constructor(private val id: UUID) : UUIDIdentity(id) {

    companion object Factory {
        fun create(): BurracoIdentity = BurracoIdentity(UUID.randomUUID())
        fun create(uuidString: String): BurracoIdentity {
            val uuid = UUID.fromString(uuidString)
            return BurracoIdentity(uuid)
        }
    }
}

object BurracoIdentityCustomSerializer: KSerializer<BurracoIdentity> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BurracoIdentity", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BurracoIdentity) {
        val string = value.convertTo().toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): BurracoIdentity {
        val uuidString = decoder.decodeString()
        return BurracoIdentity.create(uuidString)
    }
}