package com.abaddon83.burracoGame.readModel.events

import com.abaddon83.burracoGame.readModel.models.ReadBurracoGame
import com.abaddon83.burracoGame.readModel.ports.ReadModelRepositoryPort
import com.abaddon83.burracoGame.writeModel.events.*
import com.abaddon83.utils.logs.WithLog

class EventHandler(readModelRepository: ReadModelRepositoryPort): WithLog("EventHandler") {

    val repository = readModelRepository

    fun processEvent(e: Event): Unit {
        return when (e){
            is BurracoGameEvent -> when (e) {

                is BurracoGameCreated ->  {
                    log.info("Event ${e.javaClass.simpleName} loaded in the read model")
                    repository.save(ReadBurracoGame(identity = e.key(), status = "Waiting players"))

                }
                is PlayerAdded -> {
                    log.info("Event ${e.javaClass.simpleName} loaded in the read model")
                    val item = repository.get(e.key())
                    item?.copy(status = "Waiting players")?.let {
                        repository.save(it)
                    }
                }
                is GameStarted ->{
                    log.info("Event ${e.javaClass.simpleName} loaded in the read model")
                    val item = repository.get(e.key())
                    item?.copy(status = "Game Started")?.let {
                        repository.save(it)
                    }
                }
                is CardPickedFromDeck -> {
                    log.info("Event ${e.javaClass.simpleName} loaded in the read model")
                    val item = repository.get(e.key())
                    item?.copy(status = "Game Started")?.let {
                        repository.save(it)
                    }
                }
                else -> Unit
            }

        }
    }
}