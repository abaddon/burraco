package eventsourcing.messagebus

import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.messageBus.EventPublisher
import com.abaddon83.utils.es.messageBus.Handles
import com.abaddon83.utils.logs.WithLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import org.slf4j.LoggerFactory

/**
 * Implementation of an in-memory event-bus using coroutines to registered events-handler asynchronously
 *
 * The interface to publisher is still blocking
 *
 * Setting simulateLatency (msec) simulate a distributed system, with a latency on dispatching messages
 */
class AsyncInMemoryBus(private val scope: CoroutineScope, bufferSize: Int = 100, private val simulateLatency : Long? = null): EventPublisher<Event>, WithLog() {

    private val bus = BroadcastChannel<Event>(bufferSize)

    override fun publish(event: Event) = runBlocking {
        log.debug("Publishing event {}", event)
        bus.send(event)
        log.trace("Event published")
    }

    override fun register(eventHandler: Handles<Event>) : EventPublisher<Event> {
        log.info("Registering handler: {}", eventHandler)
        scope.launch { broadcastTo(eventHandler) }
        return this
    }

    private suspend fun broadcastTo(handler: Handles<Event>) = coroutineScope {
        log.debug("Starting handler {}", handler)
        bus.consumeEach {

            if (simulateLatency != null ) {
                log.trace("Simulating {}ms latency", log.trace("Forcibly delaying handling"))
                delay(simulateLatency)
            }

            log.trace("Handler '{}' is handling '{}'", handler, it)
            handler.handle(it)
        }
    }

    fun shutdown() {
        bus.close()
    }
}
