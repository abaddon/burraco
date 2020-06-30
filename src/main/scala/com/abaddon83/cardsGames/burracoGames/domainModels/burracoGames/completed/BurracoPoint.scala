package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.completed

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoId
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.Burraco
import com.abaddon83.cardsGames.shares.decks.Card

case class BurracoPoint protected (
                         override protected val burracoId: BurracoId,
                         override protected val cards: List[Card]
                       ) extends Burraco {
}

object BurracoPoint{
  def apply(burraco: Burraco): BurracoPoint = {
    new BurracoPoint(burraco.getBurracoId(), burraco.showCards)
  }
}
