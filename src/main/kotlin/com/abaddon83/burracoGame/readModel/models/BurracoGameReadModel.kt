package com.abaddon83.burracoGame.readModel.models

import com.abaddon83.burracoGame.api.messages.CardModule
import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.readModel.ports.DocumentStorePort
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.messageBus.Handles
import com.abaddon83.utils.es.readModel.DocumentStore
import com.abaddon83.utils.logs.WithLog
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.NoSuchElementException


data class BurracoGame(
        val identity: UUID,
        val status: String,
        val players: List<BurracoPlayer>,
        val playerTurn: BurracoPlayer? = null,
        val numMazzettoAvailable: Int = 0,
        val discardPile: List<CardModule>? = listOf()
)

class BurracoGameReadModel(private val burracoGameStore: DocumentStore<BurracoGame>) {

    fun find(key: String): BurracoGame = burracoGameStore.get(key)?: throw NoSuchElementException("Key not found")
}


class BurracoGameProjection() : Handles<Event>, KoinComponent, WithLog() {
    private val burracoGameStore: DocumentStore<BurracoGame> by inject()
    override fun handle(event: Event) {
        when (event) {
            is BurracoGameCreated -> {
                val new = event.toBurracoGame()
                log.info("Add new Burraco game {} to the BurracoGameReadModel", new)
                check(burracoGameStore.get(event.identity)== null){errorMsg("The game identity with id: ${event.identity} already exist!! duplicated events??")}
                burracoGameStore.save(event.identity,event.toBurracoGame())
            }
        }
    }

    private fun BurracoGameCreated.toBurracoGame(): BurracoGame = BurracoGame(
            identity = UUID.fromString(this.identity),
            status = "WAITING",
            players = listOf())

}
