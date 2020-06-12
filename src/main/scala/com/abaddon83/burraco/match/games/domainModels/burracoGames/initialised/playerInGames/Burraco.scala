package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.burraco.`match`.games.domainModels.BurracoId
import com.abaddon83.burraco.shares.decks.Card

trait Burraco {
  protected val burracoId: BurracoId
  protected val cards: List[Card]

  def getBurracoId(): BurracoId = {
    burracoId
  }

  def showCards: List[Card] = {
    cards
  }

}
