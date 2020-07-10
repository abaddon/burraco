package com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames

import com.abaddon83.burracoGame.domainModels.*
import com.abaddon83.burracoGame.shared.burracos.Burraco
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class PlayerInGame constructor(
        override val identity: PlayerIdentity,
        private val cards: List<Card>,
        private val cardsOnTable: BurracoCardsOnTable,
        private val mazzettoTaken: Boolean = false

) : BurracoPlayer() {

    fun isMazzettoTaken(): Boolean = mazzettoTaken

    fun showMyCards(): List<Card> = cards

    fun showTrisOnTable(): List<BurracoTris> = cardsOnTable.showTris()

    fun showScalesOnTable(): List<BurracoScale> = cardsOnTable.showScale()

    fun burracoList(): List<Burraco> = cardsOnTable.burracoList()

    fun orderPlayerCards(cardsOrdered: List<Card>): PlayerInGame {
        assert(cardsOrdered.sorted() == cards.sorted()) { "The cardsOrdered doesn't contain the same player cards" }
        return copy(cards = cardsOrdered)
    }

    //pickup
    fun pickUpMazzetto(mazzetto: MazzettoDeck): PlayerInGame {
        assert(!mazzettoTaken) { MazzettoDeck }
        return addCardsOnMyCard(mazzetto.getCardList()).copy(mazzettoTaken = true)
    }

    fun addCardsOnMyCard(newCards: List<Card>): PlayerInGame =
            this.copy(cards = cards.plus(newCards))


    fun dropATris(tris: BurracoTris): PlayerInGame {

        val updatedPlayerCards = removeElements(tris.showCards(),cards) //cards.minus(tris.showCards())
        val updatedPlayerCardsOnTable = cardsOnTable.addTris(tris)

        return this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
    }

    fun dropAScale(scale: BurracoScale): PlayerInGame {
        val updatedPlayerCards = removeElements(scale.showCards(),cards) //cards.minus(scale.showCards())
        val updatedPlayerCardsOnTable = cardsOnTable.addScale(scale)

        return this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
    }

    fun dropACard(card: Card): PlayerInGame {
        assert(cards.find { c -> c == card } != null) { "This card $card is not a card of the player $this" }
        return this.copy(cards = cards.minusElement(card))
    }

    fun appendACardOnBurracoDropped(burracoIdentity: BurracoIdentity, cardsToAppend: List<Card>): PlayerInGame {
        //add the cards to an existing burraco
        val updatedPlayerCardsOnTable = cardsOnTable.appendCardOnBurraco(burracoIdentity, cardsToAppend)
        //remove the cards attached from thr player cards.
        val updatedPlayerCards = removeElements(cardsToRemove = cardsToAppend,myCards = cards)
        return this.copy(cards = updatedPlayerCards, cardsOnTable = updatedPlayerCardsOnTable)
    }

    fun totalPlayerCards(): Int = cards.size + cardsOnTable.numCardsOnTable()

    private fun removeElements(cardsToRemove:List<Card>, myCards: List<Card>): List<Card> {
        check(cardsToRemove.size <= myCards.size)
        val cardToremove = cardsToRemove.first()
        val cardsToRemoveUpdated = cardsToRemove.minusElement(cardToremove)
        val myCardsUpdated = myCards.minusElement(cardToremove)
        return when{
            cardsToRemoveUpdated.isEmpty() -> myCardsUpdated
            cardsToRemoveUpdated.isNotEmpty() -> removeElements(cardsToRemoveUpdated,myCardsUpdated)
            else -> throw Exception("removeAnElement failed")
        }
    }

    companion object Factory {
        fun create(playerIdentity: PlayerIdentity, cards: List<Card>): PlayerInGame =
                PlayerInGame(playerIdentity, cards, BurracoCardsOnTable(listOf(), listOf()))
    }
}