package com.abaddon83.cardsGames.shares.players

import java.util.UUID

import com.abaddon83.libs.ddd.AggregateUUIDIdentity

case class PlayerIdentity private (override protected val id: UUID) extends AggregateUUIDIdentity {

}

object PlayerIdentity {
  def apply(): PlayerIdentity = new PlayerIdentity(UUID.randomUUID())

  def apply(uuid: UUID): PlayerIdentity = new PlayerIdentity(uuid)

  def apply(uuidString: String): PlayerIdentity = {
    assert(AggregateUUIDIdentity.isValidUUIDString(uuidString),s"${uuidString} is not a valid UUID")
    new PlayerIdentity(UUID.fromString(uuidString))
  }
}
