package com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.domainModels.BurracoDeck
import com.abaddon83.burracoGame.domainModels.DiscardPile
import com.abaddon83.burracoGame.domainModels.MazzettoDeck
import com.abaddon83.burracoGame.domainModels.MazzettoDecks
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

class BurracoDealer(private val burracoDeck: BurracoDeck, private val burracoGame: BurracoGameWaitingPlayers) {
    private val numCardsForPlayer = 11
    private val numCardsForDiscardPile = 1
    private val numCardsMazzetto: Array<Int> = arrayOf(11,18)

    fun dealBurracoCards(): BurracoCardsDealt =
        BurracoCardsDealt(
                MazzettoDecks.create(listOf(dealCardsToFirstMazzetto(),dealCardsToSecondMazzetto())),
                dealCardsToPlayers(),
                dealCardToDiscardPile(),
                burracoDeck
        )


    private fun dealCardsToPlayers(): Map<PlayerIdentity, List<Card>> =
        burracoGame.listOfPlayers().map { player ->
            dealCardsToPlayer(player.identity())
        }.reduce{ acc, next -> acc + next }

    private fun dealCardsToFirstMazzetto(): MazzettoDeck {
        val numCards=numCardsMazzetto[burracoGame.listOfPlayers().size % 2]
        return MazzettoDeck.create(grabCards(numCards))
    }

    private fun dealCardsToSecondMazzetto(): MazzettoDeck = MazzettoDeck.create(grabCards(numCardsMazzetto[0]))



    private fun grabCards(numCards: Int): List<Card> = (1..numCards).map { _ -> burracoDeck.grabFirstCard() }.toList()

    private fun dealCardsToPlayer(playerIdentity: PlayerIdentity): Map<PlayerIdentity, List<Card>> = mapOf(Pair(playerIdentity,grabCards(numCardsForPlayer)))

    private fun dealCardToDiscardPile(): DiscardPile = DiscardPile.create(grabCards(numCardsForDiscardPile))



    companion object Factory {
        fun create(burracoGame: BurracoGameWaitingPlayers): BurracoCardsDealt {
            val deckShuffled = deckShuffle(BurracoDeck.create())
            return BurracoDealer(deckShuffled, burracoGame).dealBurracoCards()
        }

        private fun deckShuffle(deck: BurracoDeck): BurracoDeck = deck.shuffle()
    }
}

data class BurracoCardsDealt(
        val mazzettoDecks: MazzettoDecks,
        val playersCards: Map<PlayerIdentity,List<Card>>,
        val discardPile : DiscardPile,
        val burracoDeck : BurracoDeck
)