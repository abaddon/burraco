package com.abaddon83.utils.configs

import com.abaddon83.utils.logs.WithLog
import java.io.File
import java.io.FileInputStream
import java.util.*


object Config: WithLog() {
    private const val localPropertyPath: String = "./local.properties"
    private val localProperties = Properties()

    fun getProperty(propertyName: String): String? {
        if(localProperties.isEmpty) {
            log.info("Loading local.properties from: ${File("./").absolutePath}")
            localProperties.load(FileInputStream(File(localPropertyPath)))
        }
        return localProperties.getProperty(propertyName)
    }
}