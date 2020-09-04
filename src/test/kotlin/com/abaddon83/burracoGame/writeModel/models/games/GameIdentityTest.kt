package com.abaddon83.burracoGame.writeModel.models.games

import org.junit.Test
import java.util.*
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
}