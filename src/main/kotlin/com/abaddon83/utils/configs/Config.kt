package com.abaddon83.utils.configs

import com.abaddon83.utils.logs.WithLog
import com.typesafe.config.ConfigException
import java.io.File
import java.io.FileInputStream
import java.util.*


object Config : WithLog("Config") {
    private const val localPropertyPath: String = "./local.properties"
    private const val applicationConfigResource: String = "./application.conf"
    private val properties: Properties = mergeProperties(listOf(loadApplicationConf(),loadLocalProperty()))


    private fun mergeProperties(propertiesList: List<Properties>): Properties{
        val propertiesMerged = Properties()
        propertiesList.forEach{ properties ->
            propertiesMerged.putAll(properties)
        }
        return propertiesMerged;
    }

    private fun loadLocalProperty() : Properties {
        return try {
            val property = Properties()
            property.load(FileInputStream(File(localPropertyPath)))
            log.info("Local.properties file loaded from: ${File("./").absolutePath}")
            property;
        } catch (ex: Exception) {
            log.warn("Local.properties file missing, path: ${File("./").absolutePath}")
            Properties()
        }
    }

    private fun loadApplicationConf() : Properties {
        return try {
            val property = Properties()
            property.load(this::class.java.classLoader.getResource(applicationConfigResource).openStream())
            log.info("application config resource loaded")
            property;
        } catch (ex: Exception) {
            log.warn("Local.properties file missing")
            Properties()
        }
    }

    fun getProperty(propertyName: String): String? = properties.getProperty(propertyName);
}