package com.abaddon83.burracoGame.readModel.adapters

import com.abaddon83.burracoGame.readModel.ports.EventListenerPort
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort
import com.abaddon83.burracoGame.writeModel.events.BurracoGameCreated
import com.abaddon83.burracoGame.writeModel.events.Event
import com.abaddon83.utils.logs.WithLog
import com.rabbitmq.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import java.nio.charset.StandardCharsets

class EventListenerRabbitmqAdapter(override val readModelRepository: ReadModelRepositoryPort) : EventListenerPort, WithLog("EventListenerRabbitmqAdapter"){


    override fun applyEvent(e: Event) {
        eventHandle.processEvent(e)

    }

    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        val message = String(delivery.body, StandardCharsets.UTF_8)
        val event = Json.decodeFromString<Event>(message)
        applyEvent(event);
        //println("[$consumerTag] Received message: '$message'")
    }
    val cancelCallback = CancelCallback { consumerTag: String? ->
        log.warn("[$consumerTag] was canceled")
    }


//    override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
//        //val routingKey: String = envelope.getRoutingKey();
//        //val contentType: String  = properties.getContentType();
//        val deliveryTag: Long = envelope!!.getDeliveryTag();
//        val event = when (body) {
//            is ByteArray -> String(body, StandardCharsets.UTF_8)
//            else -> ""
//        }
//
//
//        println("Event received: $event")
//
//        // (process the message components here ...)
//        channel.basicAck(deliveryTag, false);
//    }


//    fun createActor(): SendChannel<Event> {
//        return actor<Event> {
//            for (event in channel) { // iterate over incoming messages
//                applyEvent(event)
//            }
//        }
//    }


}