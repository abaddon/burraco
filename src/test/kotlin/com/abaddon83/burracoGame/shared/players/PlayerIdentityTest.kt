package com.abaddon83.burracoGame.shared.players

import com.abaddon83.burracoGame.writeModel.models.BurracoTris
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentityCustomSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerIdentityTest {
    @Test
    fun `new PlayerIdentity using UUID`(){
        val expectedUUID = UUID.randomUUID()
        val identity = PlayerIdentity(expectedUUID)
        assert(identity.convertTo() == expectedUUID)
    }
    @Test
    fun `new PlayerIdentity using a valid UUID String`(){
        val expectedUUID = UUID.randomUUID()
        val identity = PlayerIdentity.create(expectedUUID.toString())
        assert(identity.convertTo() == expectedUUID)
    }
    @Test
    fun `new PlayerIdentity using a not valid UUID String should fail`(){

        assertFailsWith(IllegalArgumentException::class){
            PlayerIdentity.create("fake-UUID")
        }
    }

    @Test
    fun `given a playerIdentity when I serialise it, then I should have the same playerIdentity deserialized`() {
        val expectedUUID = UUID.randomUUID()
        val identity = PlayerIdentity.create(expectedUUID.toString())

        val jsonString = Json.encodeToString(PlayerIdentityCustomSerializer,identity);
        val deserializedTris = Json.decodeFromString<PlayerIdentity>(PlayerIdentityCustomSerializer,jsonString)
        assertEquals(identity.id,deserializedTris.id)

    }

}