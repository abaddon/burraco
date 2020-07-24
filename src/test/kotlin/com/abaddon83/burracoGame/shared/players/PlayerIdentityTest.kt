package com.abaddon83.burracoGame.shared.players

import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import org.junit.Test
import java.util.*
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

}