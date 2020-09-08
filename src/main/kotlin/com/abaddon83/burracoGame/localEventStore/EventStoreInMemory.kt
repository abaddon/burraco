package com.abaddon83.burracoGame.localEventStore

import com.abaddon83.burracoGame.readModel.ports.EventListenerPort
import com.abaddon83.burracoGame.readModel.queries.QueryHandler
import com.abaddon83.burracoGame.writeModel.events.Event


object EventStoreInMemory {

    private val eventsCache = mutableMapOf<String, List<Event>>()

    private val listeners: MutableList<EventListenerPort> = mutableListOf()

    fun getEvents(pk: String): List<Event> {
        return eventsCache.getOrDefault(pk, emptyList())
    }

    fun save(events: Iterable<Event>){
        events.forEach { event ->
            processEvents(event)
        }

        //TODO listener missing
    }

    fun addListener(listener: EventListenerPort) {
        listeners.add(listener)
    }

    private fun processEvents(event: Event) {

//        when (event) {
//            is BurracoGameEvent -> eventsCache.compute(event.key()) { _, el -> (el ?: emptyList()).plus(event) }
//        }
        eventsCache.compute(event.key()) { _, el -> (el ?: emptyList()).plus(event) }

        for (listener in listeners) {
            listener.applyEvent(event)
        }

        println("Processed Event ${event.javaClass.simpleName}")
    }

}


