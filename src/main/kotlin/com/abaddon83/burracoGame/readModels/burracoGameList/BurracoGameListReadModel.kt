package com.abaddon83.burracoGame.readModels.burracoGameList

import com.abaddon83.burracoGame.domainModels.BurracoGameCreated
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.messageBus.Handles
import com.abaddon83.utils.es.readModel.SingleDocumentStore
import com.abaddon83.utils.logs.WithLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory


data class BurracoGame(val gameIdentity: GameIdentity)

class BurracoGameListReadModel(private val burracoGameListStore: SingleDocumentStore<Iterable<BurracoGame>>) {
    fun allBurracoGames(): Iterable<BurracoGame> = burracoGameListStore.get()
}

class BurracoGameListProjection(private val burracoGameListStore: SingleDocumentStore<Iterable<BurracoGame>>) : Handles<Event>, WithLog() {
    override fun handle(event: Event) {
        when (event) {
            is BurracoGameCreated -> {
                val new = event.toBurracoGame()
                log.debug("Add new Student {} to the read-model and sort the list alphabetically by fullName", new)
                val newSortedList = ((burracoGameListStore.get()) + new).sortedBy { it.gameIdentity.convertTo() }
                burracoGameListStore.save(newSortedList)
            }
        }
    }

    private fun BurracoGameCreated.toBurracoGame() = BurracoGame(this.gameIdentity)

}
