package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.completed

import com.abaddon83.burraco.`match`.games.domainModels.BurracoId
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.Burraco
import com.abaddon83.burraco.shares.decks.Card

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

