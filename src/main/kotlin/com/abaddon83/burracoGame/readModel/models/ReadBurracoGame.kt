package com.abaddon83.burracoGame.readModel.models

import java.util.*


sealed class ReadEntity

data class ReadBurracoGame(val identity: String, val status: String): ReadEntity()


//class BurracoGameListProjection(private val burracoGameListStore: DocumentStorePort<Iterable<BurracoGame>>) :  WithLog() {
//    override fun handle(event: Event) {
//        when (event) {
//            is BurracoGameCreated -> {
//      //          val new = event.toBurracoGame()
//        //        log.debug("Add new Student {} to the read-model and sort the list alphabetically by fullName", new)
//                //val newSortedList = ((burracoGameListStore.get()) + new).sortedBy { it.gameIdentity.convertTo() }
//                //burracoGameListStore.save(newSortedList)
//            }
//        }
//    }
//
//    //private fun BurracoGameCreated.toBurracoGame() = BurracoGame(this.gameIdentity)
//

