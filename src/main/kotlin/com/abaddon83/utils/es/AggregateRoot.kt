package com.abaddon83.utils.es

import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.ddd.Entity
import com.abaddon83.utils.ddd.Identity
import com.abaddon83.utils.ddd.UUIDIdentity
import com.abaddon83.utils.logs.WithLog
import java.util.*

interface AggregateType


abstract class AggregateRoot<T>(): Entity<T>() {

    abstract fun aggregateType(): AggregateType

    private val uncommittedChanges = ArrayList<Event>()

    fun getUncommittedChanges(): Iterable<Event> = uncommittedChanges.toList().asIterable()

    fun markChangesAsCommitted() {
        log.debug("Marking all changes as committed")
        uncommittedChanges.clear()
    }

    fun <A: AggregateRoot<T>> applyAndQueueEvent(event: Event): A {
        log.debug("Applying {}", event)
        val updatedAggregate = applyEvent(event) // Remember: this never fails

        log.debug("Queueing uncommitted change {}", event)
        uncommittedChanges.add(event)
        return updatedAggregate as A
    }

    protected abstract fun applyEvent(event: Event): AggregateRoot<T>

    fun <A: AggregateRoot<T>> loadFromHistory(aggregate: A, history: Iterable<Event>): A {
        log.debug("Reloading aggregate {} state from history", aggregate)

        // Rebuilding an Aggregate state from Events is a 'fold' operation
        return history.fold( aggregate, { agg, event  -> agg.applyEvent(event) as A })
    }

//    companion object Factory: WithLog(){
//
//        /**
//         * Rebuild the state of the Aggregate from its Events
//         */
//        fun <A: AggregateRoot> loadFromHistory(aggregate: A, history: Iterable<Event>): A {
//            log.debug("Reloading aggregate {} state from history", aggregate)
//
//            // Rebuilding an Aggregate state from Events is a 'fold' operation
//            return history.fold( aggregate, { agg, event  -> agg.applyEvent(event) as A })
//        }
//    }
}

class UnsupportedEventException(eventClass: Class<out Event>)
    : Exception("Unsupported event ${eventClass.canonicalName}")
