package com.abaddon83.burracoGame.domainModels


data class MazzettoDecks private constructor(val list: List<MazzettoDeck>) {

    fun firstMazzettoAvailable(): MazzettoDeck {
        assert(list.isNotEmpty()){"Mazzetto list empty, all Mazzetto taken"}
        return list.first()
    }

    fun mazzettoTaken(mazzettoDeck: MazzettoDeck): MazzettoDecks {
        assert(list.find{m -> m == mazzettoDeck}!= null) {"MazzettoDeck not found"}
        return copy(list = list.minus(mazzettoDeck))
    }

    fun numCards(): Int = list.map{m -> m.numCards()}.fold(0){ total, item -> total + item }

    companion object Factory {
        fun create(list: List<MazzettoDeck>): MazzettoDecks {
            assert(list.size == 2) {"MazzettoDecks can accept only 2 MazzettoDecks"}
            return MazzettoDecks(list.sortedBy {p-> p.numCards()}.reversed())
        }
    }
}