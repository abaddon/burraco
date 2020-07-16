package com.abaddon83.utils.es

import com.abaddon83.utils.es.messageBus.EventPublisher

/**
 * Store event streams in memory
 * This is not meant to be optimised
 */
class InMemoryEventStore<T>(eventPublisher: EventPublisher<Event>) : BaseEventStore<T>(eventPublisher) {

    private val streams: MutableMap<StreamKey<T>, MutableList<EventDescriptor<T>>> = mutableMapOf()

    override fun stream(key: StreamKey<T>): Iterable<EventDescriptor<T>>? {
        return streams[key]?.toList() ?: listOf<EventDescriptor<T>>()
    }

    override fun appendEventDescriptor(key: StreamKey<T>, eventDescriptor: EventDescriptor<T>) {
        val stream = streams[key] ?: mutableListOf()
        stream.add(eventDescriptor)
        streams[key] = stream
    }

    fun uploadEvents(aggregate: AggregateRoot<T>,events: List<Event>){
        val streamKey = StreamKey(aggregate.aggregateType(), aggregate.identity())
        var eventVersion: Long = 0
        val list: MutableList<EventDescriptor<T>> = events.map { event ->
            eventVersion++
            EventDescriptor(streamKey, eventVersion, event.copyWithVersion(version = eventVersion))
        }.toMutableList()
        list.forEach { it -> println("version: ${it.version}  event: ${it.event.javaClass.simpleName}") }
        streams[streamKey] = list
    }
}
