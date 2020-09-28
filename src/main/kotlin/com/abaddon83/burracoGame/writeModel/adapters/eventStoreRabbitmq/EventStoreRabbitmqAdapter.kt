package com.abaddon83.burracoGame.writeModel.adapters.eventStoreRabbitmq

import com.abaddon83.burracoGame.localEventStore.EventStoreInMemory
import com.abaddon83.burracoGame.writeModel.events.BurracoGameEvent
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.burracoGame.writeModel.ports.EventStore
import com.abaddon83.utils.rabbitmq.RabbitMqClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventStoreRabbitmqAdapter : EventStore() {

    private val eventStore: EventStoreInMemory = EventStoreInMemory
    override fun save(events: Iterable<Event>) {
        eventStore.save(events)
        events.forEach { event ->
            RabbitMqClient.sendMessage("burraco", Json.encodeToString(event))
        }
    }

    override fun getBurracoGameEvents(pk: String): List<BurracoGameEvent> {
        return eventStore.getEvents(pk).map { event ->
            when (event) {
                is BurracoGameEvent -> event
            }
        }
    }
}
