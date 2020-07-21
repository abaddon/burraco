package com.abaddon83.utils.es.repository

import com.abaddon83.utils.es.AggregateRoot
import com.abaddon83.utils.es.eventStore.EventStore
import com.abaddon83.utils.logs.WithLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Repository implementing persistence through Event Sourcing:
 * - save uncommited changes (Events) of an Aggregate
 * - rebuild Aggregate from its Events
 */
abstract class EventSourcedRepository<T,A : AggregateRoot<T>>(eventStore: EventStore<T>) : Repository<T, A>, WithLog() {

    private val store = eventStore

    override fun save(aggregate: A, expectedVersion: Long?): A {
        log.debug("Storing ${aggregate.getUncommittedChanges().toList().size} uncommitted event for '${aggregate.aggregateType()}:$aggregate.id'")
        store.saveEvents(aggregateType = aggregate.aggregateType(), aggregateId = aggregate.identity(), events = aggregate.getUncommittedChanges(), expectedVersion = expectedVersion)
        aggregate.markChangesAsCommitted()
        return aggregate
    }

    override fun getById(id: T): A {

        val aggregate = new(id)
        val aggregateType = aggregate.aggregateType()
        log.debug("Retrieve {} by id:{}", aggregateType, id)
        val events = store.getEventsForAggregate(aggregate.aggregateType(), id)?: listOf()
        check(events.toList().isNotEmpty()){ warnMsg("Aggregate $id doesn't exist")}
        return aggregate.loadFromHistory(aggregate, events)

    }

    override fun exist(id: T): Boolean {
        return try {
            getById(id)
            true
        } catch (ex: Exception){
            false
        }
    }
}