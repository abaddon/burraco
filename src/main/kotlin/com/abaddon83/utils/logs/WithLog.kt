package com.abaddon83.utils.logs

import org.slf4j.LoggerFactory

open class WithLog {
    protected val log = LoggerFactory.getLogger(javaClass.simpleName)
}