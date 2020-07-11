package com.abaddon83.utils.logs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WithLog {
    protected val log: Logger = LoggerFactory.getLogger("com.abaddon83")
}