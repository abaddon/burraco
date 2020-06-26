package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.runnings.playerInGame

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoDeck
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.BurracoTris
import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import com.abaddon83.burraco.shares.decks.Ranks.Rank
import org.scalatest.funsuite.AnyFunSuite

class BurracoTrisTest extends AnyFunSuite{
  test("given a tris, when I add a card with the same rank, the tris increase the size") {
    val burracoTrisRank = Ranks.Five
    val burracoTrisSize= 3

    val cards = List(Card(suit = Suits.Heart,rank = burracoTrisRank))
    val burracoTris = createABurracoTrisWith(burracoTrisRank,burracoTrisSize)

    val actualBurracoTris=burracoTris.addCards(cards)

    assert(actualBurracoTris.showCards.size == (burracoTrisSize+1))
    assert(actualBurracoTris.showCards.contains(cards(0)))

  }


  private def createABurracoTrisWith(rank: Rank,numCards: Int): BurracoTris ={
    BurracoTris(
      cards = BurracoDeck().grabAllCards().filter(c => c.rank == rank).take(numCards)
    )
  }



}
