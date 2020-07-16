package com.abaddon83.utils.es.messageBus

import com.abaddon83.utils.es.Event

interface EventPublisher<E : Event> {
    fun publish(event : E)
    fun register(eventHandler: Handles<E>) : EventPublisher<E>
}

interface Handles<in E : Event> {
    fun handle(event : E)
}