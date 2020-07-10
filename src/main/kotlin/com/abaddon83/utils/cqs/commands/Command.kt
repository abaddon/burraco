package com.abaddon83.utils.cqs.commands

import java.sql.Timestamp
import java.util.*

interface Command {
    val commandId: UUID
    val commandTimeStamp: Timestamp
}