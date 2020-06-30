package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoId
import com.abaddon83.cardsGames.shares.decks.Card

trait Burraco {
  protected val burracoId: BurracoId
  protected val cards: List[Card]

  def getBurracoId(): BurracoId = {
    burracoId
  }

  def showCards(): List[Card] = cards

  def isBurraco(): Boolean = {
    if(cards.size < 7) {
      false
    }else{
      true
    }
  }

}
