package com.abaddon83.burracoGame.readModel.adapters

import com.abaddon83.burracoGame.readModel.ports.EventListenerPort
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort
import com.abaddon83.burracoGame.writeModel.events.Event

class EventListenerAdapter(override val readModelRepository: ReadModelRepositoryPort) : EventListenerPort {
    override fun applyEvent(e: Event) {
        eventHandle.processEvent(e)
    }

}