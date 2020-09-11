package com.abaddon83.burracoGame.readModel.adapters

import com.abaddon83.burracoGame.readModel.ports.EventListenerPort
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort
import com.abaddon83.burracoGame.writeModel.events.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

class EventListenerAdapter(override val readModelRepository: ReadModelRepositoryPort) : EventListenerPort, CoroutineScope {

    private val job = Job()
    override val coroutineContext = Dispatchers.Unconfined + job

    override fun applyEvent(e: Event) {
        eventHandle.processEvent(e)
    }

    fun createActor(): SendChannel<Event> {
        return actor<Event> {
            for (event in channel) { // iterate over incoming messages
                applyEvent(event)
            }
        }
    }
}