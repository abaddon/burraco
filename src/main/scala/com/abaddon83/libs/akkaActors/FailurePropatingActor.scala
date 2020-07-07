package com.abaddon83.libs.akkaActors

import akka.actor.{Actor, Status}

trait FailurePropatingActor extends Actor {
  override def preRestart(reason: Throwable, message: Option[Any])
  {
    super.preRestart(reason, message)
    sender() ! Status.Failure(reason)
  }
}