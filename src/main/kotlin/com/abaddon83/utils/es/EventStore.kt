package com.abaddon83.utils.es


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
