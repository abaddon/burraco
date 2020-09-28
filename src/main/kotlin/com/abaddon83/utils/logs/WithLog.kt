package com.abaddon83.utils.logs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WithLog(className: String) {
    constructor(): this("com.abaddon83")

    protected val log: Logger = LoggerFactory.getLogger(className)

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