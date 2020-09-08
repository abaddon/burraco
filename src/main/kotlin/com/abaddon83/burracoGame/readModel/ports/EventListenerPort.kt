package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.readModel.events.EventHandler
import com.abaddon83.burracoGame.writeModel.events.Event

interface EventListenerPort {

    val readModelRepository: ReadModelRepositoryPort

    val eventHandle: EventHandler
        get() = EventHandler(readModelRepository = readModelRepository)

    fun applyEvent(e: Event)
}