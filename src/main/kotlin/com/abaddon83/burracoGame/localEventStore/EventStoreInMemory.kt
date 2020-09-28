package com.abaddon83.burracoGame.localEventStore

import com.abaddon83.burracoGame.readModel.ports.EventListenerPort
import com.abaddon83.burracoGame.readModel.queries.QueryHandler
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.utils.logs.WithLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch


object EventStoreInMemory: WithLog("EventStoreInMemory") {

    private val eventsCache = mutableMapOf<String, List<Event>>()

    private val syncListeners: MutableList<EventListenerPort> = mutableListOf()

    private val asyncListeners: MutableList<SendChannel<Event>> = mutableListOf()

    fun getEvents(pk: String): List<Event> {
        return eventsCache.getOrDefault(pk, emptyList())
    }

    fun save(events: Iterable<Event>) {
        events.forEach { event ->
            processEvents(event)
        }
    }

    fun addListener(listener: EventListenerPort) {
        syncListeners.add(listener)
    }

    fun addListener(listener: SendChannel<Event>) {
        asyncListeners.add(listener)
    }

    private fun processEvents(event: Event) {

        eventsCache.compute(event.key()) { _, el -> (el ?: emptyList()).plus(event) }

        for (listener in syncListeners) {
            listener.applyEvent(event)
        }

        GlobalScope.launch {
            for (listener in asyncListeners) {
                listener.send(event)
            }
        }

        log.debug("Processed Event ${event.javaClass.simpleName}")
    }

}


