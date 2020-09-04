package com.abaddon83.burracoGame.writeModel.models.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.writeModel.models.*
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity

class BurracoDealer(val burracoDeck: BurracoDeck, private val burracoPlayers: List<BurracoPlayer>) {

    companion object Factory {
        fun create(burracoDeck: BurracoDeck, burracoPlayers: List<BurracoPlayer>): BurracoDealer {
            val deckShuffled = deckShuffle(BurracoDeck.create())
            return BurracoDealer(deckShuffled, burracoPlayers)
        }


        private fun deckShuffle(deck: BurracoDeck): BurracoDeck = deck.shuffle()
    }

    private val numCardsForPlayer = 11
    private val numCardsForDiscardPile = 1
    private val numCardsMazzetto: Array<Int> = arrayOf(11, 18)

    val dealCardsToPlayers: Map<PlayerIdentity, List<Card>> = burracoPlayers.map { player ->
        dealCardsToPlayer(player.identity())
    }.reduce { acc, next -> acc + next }

    val dealCardsToFirstMazzetto: MazzettoDeck = MazzettoDeck.create(grabCards(numCardsMazzetto[burracoPlayers.size % 2]))
    val dealCardsToSecondMazzetto: MazzettoDeck = MazzettoDeck.create(grabCards(numCardsMazzetto[0]))
    val dealCardToDiscardPile: DiscardPile = DiscardPile.create(grabCards(numCardsForDiscardPile))

    private fun grabCards(numCards: Int): List<Card> = (1..numCards).map { _ -> burracoDeck.grabFirstCard() }.toList()

    private fun dealCardsToPlayer(playerIdentity: PlayerIdentity): Map<PlayerIdentity, List<Card>> = mapOf(Pair(playerIdentity, grabCards(numCardsForPlayer)))

}