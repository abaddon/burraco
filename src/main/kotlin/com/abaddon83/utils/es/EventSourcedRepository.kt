package com.abaddon83.utils.es

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Repository implementing persistence through Event Sourcing:
 * - save uncommited changes (Events) of an Aggregate
 * - rebuild Aggregate from its Events
 */
abstract class EventSourcedRepository<T,A : AggregateRoot<T>>(eventStore: EventStore<T>) : Repository<T, A> {

    private val store = eventStore

    override fun save(aggregate: A, expectedVersion: Long?): Boolean {
        log.debug("Storing uncommitted event for '${aggregate.aggregateType()}:$aggregate.id'")
        store.saveEvents(aggregateType = aggregate.aggregateType(), aggregateId = aggregate.identity(), events = aggregate.getUncommittedChanges(), expectedVersion = expectedVersion)
        aggregate.markChangesAsCommitted()
        return true
    }

    override fun getById(id: T): A {

        val aggregate = new(id)
        val aggregateType = aggregate.aggregateType()
        log.debug("Retrieve {} by id:{}", aggregateType, id)
        val events = checkNotNull(store.getEventsForAggregate(aggregate.aggregateType(), id))
        return aggregate.loadFromHistory(aggregate, events)

    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(EventSourcedRepository::class.java)
    }
}