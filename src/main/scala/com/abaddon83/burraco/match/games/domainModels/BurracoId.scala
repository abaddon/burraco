package com.abaddon83.burraco.`match`.games.domainModels

import java.util.UUID

case class BurracoId(id: UUID)

object BurracoId{
  def apply(): BurracoId = new BurracoId(UUID.randomUUID())
}
