package com.abaddon83.burracoGame.readModel.ports

import com.abaddon83.burracoGame.readModel.events.EventHandler
import com.abaddon83.burracoGame.writeModel.events.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel

interface EventListenerPort {

    val readModelRepository: ReadModelRepositoryPort

    val eventHandle: EventHandler
        get() = EventHandler(readModelRepository = readModelRepository)

    fun applyEvent(e: Event)

}