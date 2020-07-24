package com.abaddon83.utils.logs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WithLog {
    protected val log: Logger = LoggerFactory.getLogger("com.abaddon83")

    fun errorMsg(error: String): String{
        log.error(error)
        return error
    }

    fun warnMsg(error: String): String{
        log.warn(error)
        return error
    }

    fun debugMsg(error: String): String{
        log.debug(error)
        return error
    }
}