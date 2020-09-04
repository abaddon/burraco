package com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories

import com.abaddon83.burracoGame.writeModel.events.BurracoGameEvent
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.ports.EventStore
//import kotlinx.coroutines.channels.SendChannel


class EventStoreInMemory : EventStore() {
    private val burracoGameEventCache = mutableMapOf<String, List<BurracoGameEvent>>()
    //private val listeners: MutableList<SendChannel<Event>> = mutableListOf()

    //override val sendChannel: SendChannel<Iterable<Event>> = processEvents(event: Event)

    override fun getBurracoGameEvents(pk: String): List<BurracoGameEvent> {
        return burracoGameEventCache.getOrDefault(pk, emptyList())
    }

    override fun save(events: Iterable<Event>){
        events.forEach { event ->
            processEvents(event)
        }

        //TODO listener missing
    }

    private fun processEvents(event: Event) {

        when (event) {
            is BurracoGameEvent -> burracoGameEventCache.compute(event.key()) { _, el -> (el ?: emptyList()).plus(event) }
        }

        //for (listener in listeners) {
        //    listener.send(event)
        //}

        println("Processed Event ${event.javaClass.simpleName}")
    }

//    override fun addListener(listener: SendChannel<Event>) {
//        listeners.add(listener)
//    }

    fun saveAllEvents() {
        //persist all events
        //not implemented

    }

    fun loadAllEvents() {
        //load all events from persistence
        //not implemented
    }

}


