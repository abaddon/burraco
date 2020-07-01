package com.abaddon83.cardsGames.burracoGames.domainModels

import java.util.UUID

case class BurracoId(id: UUID)

object BurracoId{
  def apply(): BurracoId = new BurracoId(UUID.randomUUID())
}
