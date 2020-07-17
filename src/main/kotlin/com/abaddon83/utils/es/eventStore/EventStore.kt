package com.abaddon83.utils.es.eventStore

import com.abaddon83.utils.es.AggregateType
import com.abaddon83.utils.es.Event


/**
 * Interface for a simple Event Store
 */
interface EventStore<T> {
    fun saveEvents(
            aggregateType: AggregateType,
            aggregateId: T,
            events: Iterable<Event>,
            expectedVersion: Long? = null) : Iterable<Event>

    fun getEventsForAggregate(aggregateType: AggregateType, aggregateId: T): Iterable<Event>?
}


sealed class EventStoreFailure {
    object ConcurrentChangeDetected : EventStoreFailure()
}
