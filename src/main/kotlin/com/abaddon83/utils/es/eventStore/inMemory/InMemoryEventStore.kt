package com.abaddon83.utils.es.eventStore.inMemory

import com.abaddon83.utils.es.AggregateRoot
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.eventStore.BaseEventStore
import com.abaddon83.utils.es.messageBus.EventPublisher

/**
 * Store event streams in memory
 * This is not meant to be optimised
 */
class InMemoryEventStore<T>(eventPublisher: EventPublisher<Event>) : BaseEventStore<T>(eventPublisher) {

    private val streams: MutableMap<StreamKey<T>, List<EventDescriptor<T>>> = mutableMapOf()

    override fun stream(key: StreamKey<T>): Iterable<EventDescriptor<T>>? {
        return streams[key]?.toList() ?: listOf<EventDescriptor<T>>()
    }

    override fun appendEventDescriptor(eventDescriptor: EventDescriptor<T>) {
        val key = eventDescriptor.streamKey
        val stream = streams[key] ?: mutableListOf()
        streams[key] = stream.plus(eventDescriptor)
    }


    //TEST
    fun uploadEvents(aggregate: AggregateRoot<T>, events: List<Event>){
        val streamKey = StreamKey(aggregate.aggregateType(), aggregate.identity())
        saveEvents(aggregate.aggregateType(),aggregate.identity(),events)
        //var eventVersion: Long = 0
        //val list: MutableList<EventDescriptor<T>> = events.map { event ->
        //    eventVersion++
        //    EventDescriptor(streamKey, eventVersion, event.assignVersion(version = eventVersion))
        //}.toMutableList()
        //list.forEach { it -> println("version: ${it.version}  event: ${it.event.javaClass.simpleName}") }

        //streams[streamKey] = list
    }
}
