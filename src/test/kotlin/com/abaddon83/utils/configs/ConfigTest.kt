package com.abaddon83.utils.configs

import org.junit.Test
import kotlin.test.assertEquals

class ConfigTest {

    @Test
    fun `test the property priority`(){
        assertEquals(Config.getProperty("test.value"),"resource")
        assertEquals(Config.getProperty("test.value1"),null)
    }
}