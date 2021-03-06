package com.abaddon83.burracoGame.writeModel.ports

import com.abaddon83.burracoGame.writeModel.events.BurracoGameEvent
import com.abaddon83.burracoGame.writeModel.events.Event


abstract class EventStore {

    abstract fun save(events: Iterable<Event>)

    inline fun <reified T: Event> getEvents(pk: String): List<T> =
            when (T::class) {
                BurracoGameEvent::class ->  getBurracoGameEvents(pk) as List<T>
                else -> emptyList()
            }

    abstract fun getBurracoGameEvents(pk: String): List<BurracoGameEvent>
}