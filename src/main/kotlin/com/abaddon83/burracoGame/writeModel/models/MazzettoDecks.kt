package com.abaddon83.burracoGame.writeModel.models

import com.abaddon83.utils.logs.WithLog


data class MazzettoDecks private constructor(val list: List<MazzettoDeck>): WithLog() {

    fun firstMazzettoAvailable(): MazzettoDeck {
        check(list.isNotEmpty()){ warnMsg("Mazzetto list empty, all Mazzetto taken")}
        return list.first()
    }

    fun mazzettoTaken(mazzettoDeck: MazzettoDeck): MazzettoDecks {
        check(list.find{m -> m == mazzettoDeck}!= null) {errorMsg("MazzettoDeck not found")}
        return copy(list = list.minus(mazzettoDeck))
    }

    fun numCards(): Int = list.map{m -> m.numCards()}.fold(0){ total, item -> total + item }

    companion object Factory {
        fun create(list: List<MazzettoDeck>): MazzettoDecks {
            require(list.size == 2) {"MazzettoDecks can accept only 2 MazzettoDecks"}
            return MazzettoDecks(list.sortedBy { p -> p.numCards() }.reversed())
        }
    }
}