package com.abaddon83.burracoGame.readModel.models

import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.readModel.ports.DocumentStorePort
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.messageBus.Handles
import com.abaddon83.utils.es.readModel.SingleDocumentStore
import com.abaddon83.utils.logs.WithLog


data class BurracoGame(val gameIdentity: GameIdentity)

class BurracoGameListReadModel(private val burracoGameListStore: SingleDocumentStore<Iterable<BurracoGame>>) {
    fun allBurracoGames(): Iterable<BurracoGame> = burracoGameListStore.get()
}


class BurracoGameListProjection(private val burracoGameListStore: DocumentStorePort<Iterable<BurracoGame>>) : Handles<Event>, WithLog() {
    override fun handle(event: Event) {
        when (event) {
            is BurracoGameCreated -> {
                val new = event.toBurracoGame()
                log.debug("Add new Student {} to the read-model and sort the list alphabetically by fullName", new)
                //val newSortedList = ((burracoGameListStore.get()) + new).sortedBy { it.gameIdentity.convertTo() }
                //burracoGameListStore.save(newSortedList)
            }
        }
    }

    private fun BurracoGameCreated.toBurracoGame() = BurracoGame(this.gameIdentity)

}
