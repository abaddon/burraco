package com.abaddon83.burracoGame.writeModel.models.games

import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentityCustomSerializer
import kotlinx.serialization.json.Json
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameIdentityTest {
    @Test
    fun `new GameIdentity using UUID`(){
        val expectedUUID = UUID.randomUUID()
        val identity = GameIdentity(expectedUUID)
        assert(identity.convertTo() == expectedUUID)
    }
    @Test
    fun `new GameIdentity using a valid UUID String`(){
        val expectedUUID = UUID.randomUUID()
        val identity = GameIdentity.create(expectedUUID.toString())
        assert(identity.convertTo() == expectedUUID)
    }
    @Test
    fun `new GameIdentity using a not valid UUID String should fail`(){

        assertFailsWith(IllegalArgumentException::class){
            GameIdentity.create("fake-UUID")
        }
    }

    @Test
    fun `given a GameIdentity when I serialise it, then I should have the same GameIdentity deserialized`() {
        val expectedUUID = UUID.randomUUID()
        val identity = GameIdentity.create(expectedUUID.toString())

        val jsonString = Json.encodeToString(GameIdentityCustomSerializer,identity);
        val deserializedTris = Json.decodeFromString<GameIdentity>(GameIdentityCustomSerializer,jsonString)
        assertEquals(identity.id,deserializedTris.id)

    }
}