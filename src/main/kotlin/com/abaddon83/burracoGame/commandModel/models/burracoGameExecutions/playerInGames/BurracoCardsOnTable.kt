package com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.playerInGames

import com.abaddon83.burracoGame.commandModel.models.BurracoScale
import com.abaddon83.burracoGame.commandModel.models.BurracoTris
import com.abaddon83.burracoGame.commandModel.models.burracos.Burraco
import com.abaddon83.burracoGame.commandModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.commandModel.models.decks.Card

data class BurracoCardsOnTable(
        private val listOfTris: List<BurracoTris>,
        private val listOfScale: List<BurracoScale>
) {

    fun burracoList(): List<Burraco> = listOfTris.filter { t -> t.isBurraco() }
            .plus(listOfScale.filter { s -> s.isBurraco() })

    fun numCardsOnTable(): Int =
            listOfTris.map { tris -> tris.showCards().size }.fold(0) { total, item -> total + item } +
                    listOfScale.map { scale -> scale.showCards().size }.fold(0) { total, item -> total + item }

    fun showTris(): List<BurracoTris> = listOfTris

    fun showScale(): List<BurracoScale> = listOfScale

    fun addTris(trisToAdd: BurracoTris): BurracoCardsOnTable = this.copy(listOfTris = listOfTris.plus(trisToAdd))

    fun addScale(scaleToAdd: BurracoScale): BurracoCardsOnTable = this.copy(listOfScale = listOfScale.plus(scaleToAdd))

    fun appendCardOnBurraco(burracoIdentity: BurracoIdentity, cardsToAppend: List<Card>): BurracoCardsOnTable =
        when {
            listOfScale.find { s -> s.identity() == burracoIdentity } != null -> appendCardOnScale(burracoIdentity, cardsToAppend)
            listOfTris.find { t -> t.identity() == burracoIdentity } != null -> appendCardOnTris(burracoIdentity, cardsToAppend)
            else -> throw NoSuchElementException("The $burracoIdentity doesn't exist")
        }

    private fun appendCardOnScale(burracoId: BurracoIdentity, cardsToAppend: List<Card>): BurracoCardsOnTable {
        val listOfBurracoScale = listOfScale.map { scale ->
            when (burracoId) {
                scale.identity() -> scale.addCards(cardsToAppend)
                else -> scale
            }
        }
        return copy(listOfScale = listOfBurracoScale)
    }

    private fun appendCardOnTris(burracoIdentity: BurracoIdentity, cardsToAppend: List<Card>): BurracoCardsOnTable {
        val listOfBurracoTris = listOfTris.map { tris ->
            when (burracoIdentity) {
                tris.identity() -> tris.addCards(cardsToAppend)
                else -> tris
            }
        }
        return copy(listOfTris = listOfBurracoTris)
    }
}