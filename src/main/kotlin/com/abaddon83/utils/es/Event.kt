package com.abaddon83.utils.es

import java.time.Instant

abstract class Event(private val version: Long?, val eventTime: Instant =  Instant.now()) {
    // It would be better to inject a clock, but we have no logic to test around eventTime

    // The version is assigned only then the Event is stored in the EventStore
    // There are effectively two types of Events: before and after they are stored in the Event Store.
    // TODO find a more elegant solution for event version
    fun version(): Long? = version
    abstract fun copyWithVersion(version: Long): Event
}