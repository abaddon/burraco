package com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories

import com.abaddon83.burracoGame.localEventStore.EventStoreInMemory
import com.abaddon83.burracoGame.writeModel.events.BurracoGameEvent
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.ports.EventStore


class EventStoreInMemoryAdapter : EventStore() {

    private  val eventStore: EventStoreInMemory =  EventStoreInMemory
    override fun save(events: Iterable<Event>) {
        eventStore.save(events)
    }

    override fun getBurracoGameEvents(pk: String): List<BurracoGameEvent> {
        return eventStore.getEvents(pk).map { event ->
            when(event){
                is BurracoGameEvent -> event
            }
        }
    }
}


