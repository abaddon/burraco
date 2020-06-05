package com.abaddon83.burraco.shares.games

import java.util.UUID

import com.abaddon83.libs.ddd.AggregateUUIDIdentity

case class GameIdentity private(override protected val id: UUID) extends AggregateUUIDIdentity {

}

object GameIdentity {
  def apply(): GameIdentity = new GameIdentity(UUID.randomUUID())

  def apply(uuid: UUID): GameIdentity = new GameIdentity(uuid)

  def apply(uuidString: String): GameIdentity = {
    assert(AggregateUUIDIdentity.isValidUUIDString(uuidString),s"${uuidString} is not a valid UUID")
    new GameIdentity(UUID.fromString(uuidString))
  }
}