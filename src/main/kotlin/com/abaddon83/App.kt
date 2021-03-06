/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.abaddon83

import com.abaddon83.burracoGame.writeModel.ports.WriteModelControllerPort
import com.abaddon83.burracoGame.api.handleExceptions.errorsHandling
import com.abaddon83.burracoGame.readModel.adapters.EventListenerRabbitmqAdapter
import com.abaddon83.burracoGame.readModel.adapters.ReadModelRepositoryInMemoryAdapter
import com.abaddon83.burracoGame.readModel.adapters.readModelRestAdapter.ReadModelControllerAdapter
import com.abaddon83.burracoGame.readModel.adapters.readModelRestAdapter.queryApiBurracoGames
import com.abaddon83.burracoGame.readModel.ports.ReadModelControllerPort
import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.WriteModelControllerRestAdapter
import com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.commandApiBurracoGames
import com.abaddon83.burracoGame.writeModel.adapters.eventStoreRabbitmq.EventStoreRabbitmqAdapter
import com.abaddon83.burracoGame.writeModel.ports.EventStore
import com.abaddon83.utils.configs.Config
import com.abaddon83.utils.rabbitmq.RabbitMqClient
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.jackson.*
import io.ktor.routing.Routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.main() {
    //read model
    val readModelRepository= ReadModelRepositoryInMemoryAdapter()

    //RABBITMQ version
    val readModelEventListener = EventListenerRabbitmqAdapter(readModelRepository);
    val queueName: String= Config.getProperty("rabbitmq.queue.events").orEmpty()
    RabbitMqClient.addListener(queueName,"readModel",readModelEventListener.deliverCallback,readModelEventListener.cancelCallback);

    //IN MEMORY VERSION
    //val readModelEventListener = EventListenerAdapter(readModelRepository)
    //EventStoreInMemory.addListener(readModelEventListener) //sync version
    //EventStoreInMemory.addListener(readModelEventListener.createActor())

    val burracoGameReadModelController: ReadModelControllerPort = ReadModelControllerAdapter(readModelRepository)

    // write model
    //eventStore adapter
    //val eventStore: EventStore = EventStoreInMemoryAdapter() //eventStore adapter

    //eventStore adapter with RabbitMQ
    val eventStore: EventStore = EventStoreRabbitmqAdapter()

    val burracoGameWriteModelController: WriteModelControllerPort = WriteModelControllerRestAdapter(eventStore)


    //HTTP
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            register(ContentType.Application.Json, JacksonConverter())
        }
    }

    install(StatusPages) {
        errorsHandling()
    }
    install(Routing) {
        commandApiBurracoGames(burracoGameWriteModelController)
        queryApiBurracoGames(burracoGameReadModelController)
    }

}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}
