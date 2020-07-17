package com.abaddon83.utils.es

import com.abaddon83.utils.es.messageBus.EventPublisher
import com.abaddon83.utils.logs.WithLog

/**
 * Implements most of the logic of the Event Store, keeping the actual storage abstract
 */
abstract class BaseEventStore<T>(private val eventPublisher : EventPublisher<Event>) : EventStore<T>, WithLog() {
    protected data class StreamKey<T>(val aggregateType: AggregateType, val aggregateID: T) {
        override fun toString(): String = "$aggregateType:$aggregateID"
    }

    protected data class EventDescriptor<T>(val streamKey: StreamKey<T>, val version: Long, val event: Event)

    protected abstract fun stream(key: StreamKey<T>): Iterable<EventDescriptor<T>>?

    protected abstract fun appendEventDescriptor(eventDescriptor: EventDescriptor<T>)


    override fun getEventsForAggregate(aggregateType: AggregateType, aggregateId: T): Iterable<Event>? {
        log.debug("Retrieving events for aggregate {}:{}", aggregateType, aggregateId)
        val key = StreamKey(aggregateType, aggregateId)
        return stream(key)?.map { e -> e.event }
    }

    override fun saveEvents(aggregateType: AggregateType, aggregateId: T, events: Iterable<Event>, expectedVersion: Long?) : Iterable<Event> {
        val streamKey = StreamKey(aggregateType, aggregateId)
        log.debug("Saving new events for {}. Expected version: {}", streamKey, expectedVersion)

//        return if ( stream(streamKey)?.concurrentChangeDetected(expectedVersion) ) {
//            log.debug("Concurrent change detected")
//            Left(EventStoreFailure.ConcurrentChangeDetected)
//        } else {
            log.debug("Appending and publishing {} events", events.count())
           return appendAndPublish(streamKey, events, expectedVersion)
//        }
    }


//    private fun Iterable<EventDescriptor>.concurrentChangeDetected(expectedVersion: Long?) : Boolean =
//            expectedVersion?.let { expVersion ->
//                this.map { events -> events.last() }
//            }
//            expectedVersion?.map {  expVersion ->
//                this.map { events -> events.last() }
//                        .exists { event -> event.version != expVersion }
//            }.getOrElse { false }



    private fun appendAndPublish(streamKey: StreamKey<T>, events: Iterable<Event>, previousAggregateVersion: Long? ) : Iterable<Event> {
        val baseVersion : Long = previousAggregateVersion?: -1
        return sequence {
            for ( (i, event) in events.withIndex()) {
                val eventVersion = baseVersion + i + 1

                // Events have a version when stored in a stream and published
                val versionedEvent = event.assignVersion(eventVersion)
                yield(versionedEvent)

                val eventDescriptor = EventDescriptor(streamKey, eventVersion, versionedEvent)

                log.debug("Appending event {} to Stream {}", eventDescriptor, streamKey)
                appendEventDescriptor(eventDescriptor )

                log.trace("Publishing event: {}", versionedEvent)
                eventPublisher.publish(versionedEvent)
            }
        }.toList()
    }
}
